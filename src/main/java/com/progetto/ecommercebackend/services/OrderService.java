package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.entities.Order;
import com.progetto.ecommercebackend.entities.OrderBook;
import com.progetto.ecommercebackend.entities.User;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.repositories.OrderBookRepository;
import com.progetto.ecommercebackend.repositories.OrderRepository;
import com.progetto.ecommercebackend.repositories.UserRepository;
import com.progetto.ecommercebackend.support.common.OrderForm;
import com.progetto.ecommercebackend.support.enums.OrderStatus;
import com.progetto.ecommercebackend.support.enums.OrderStatusDTO;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderBookRepository orderBookRepository;

    @Autowired
    KeycloakService keycloakService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;


    public Order getPendingCart(String userId) {
        Optional<UserRepresentation> userRepresentationOptional = keycloakService.getUserById(userId);

        if (userRepresentationOptional.isEmpty()) {
            throw new CustomException("Utente non trovato.");
        }

        UserRepresentation userRepresentation = userRepresentationOptional.get();
        User user = userRepository.findById(userRepresentation.getId())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setId(userRepresentation.getId());
                    newUser.setFirstName(userRepresentation.getFirstName());
                    newUser.setLastName(userRepresentation.getLastName());
                    return userRepository.save(newUser);
                });

        Order pendingCart = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        if (pendingCart == null) {
            pendingCart = new Order();
            pendingCart.setUser(user);
            pendingCart.setOrderStatus(OrderStatus.PENDING);
            pendingCart = orderRepository.save(pendingCart);
        }
        return pendingCart;
    }


    @Transactional
    public Order resetCart(String userId, Long orderId) {
        Optional<Order> pendingCartOptional = orderRepository.findById(orderId);
        if (pendingCartOptional.isPresent()) {
            for (OrderBook orderBook : orderBookRepository.findAllByOrderId(orderId)) {
                orderBookRepository.delete(orderBook);
            }
            return pendingCartOptional.get();
        } else {
            throw new CustomException("\"Errore nel ripristino del carrello.");
        }
    }


    @Transactional
    public Order addBookToCart(Book book, String userId) {
        Order pendingCart =  getPendingCart(userId);
        if(book == null ){
            throw new CustomException("È necessario il parametro 'book'");
        }
        Optional<OrderBook> orderBookOptional = Optional.ofNullable(orderBookRepository.findByBookIdAndOrderId(book.getId(), pendingCart.getId()));
        if( orderBookOptional.isPresent() ){
            throw new CustomException("Libro già presente nel carrello.");
        }else{
            // Decrementare la quantità di libri nell'inventario
            Book book1 = bookRepository.findBookById(book.getId());
            if( book1.getQuantity()>0){
                book1.setQuantity(book1.getQuantity()-1);
                bookRepository.save(book1);

                //Aggiungere libro nel carrello
                OrderBook orderBook = new OrderBook(pendingCart, book, 1);
                orderBookRepository.save(orderBook);
                return pendingCart;
            }else {
                throw new CustomException("Impossibile aggiungere il libro al carrello: quantità esaurita nel magazzino.");
            }
        }
    }


    @Transactional
    public Order removeBookFromCart(Book book, String userId) {
        Order pendingCart =  getPendingCart(userId);
        if(book == null ){
            throw new CustomException("È necessario il parametro 'book'");
        }
        Optional<OrderBook> orderBookOptional = Optional.ofNullable(orderBookRepository.findByBookIdAndOrderId(book.getId(), pendingCart.getId()));
        if( orderBookOptional.isPresent() ){
            OrderBook orderBook = orderBookOptional.get();
            if (orderBook.getQuantity() == 1) {
                //Rimuovere il libro dal carrello dell'utente
                orderBookRepository.delete(orderBook);
            }else{
                //Incrementare la quantità di libri nell'inventario
                Book book1 = bookRepository.findBookById(book.getId());
                book1.setQuantity(book.getQuantity()+1);
                bookRepository.save(book1);

                //Decrementare la quantità del libro presente nel carrello dell'utente
                orderBook.setQuantity(orderBook.getQuantity()-1);
                orderBookRepository.save(orderBook);
            }
            orderRepository.save(pendingCart);
            return pendingCart;
        }else{
            throw new CustomException("Il libro non è presente nel carrello.");
        }

    }

    @Transactional
    public OrderBook increaseBookQtyInCart(String userId, Long bookId) {
        Order pendingCart = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        OrderBook orderBook = orderBookRepository.findByBookIdAndOrderId(bookId, pendingCart.getId());
        if (orderBook == null){
            throw new CustomException("Il libro non è presente nel carrello.");
        }else{
            Book book1 = bookRepository.findBookById(bookId);
            if (book1.getQuantity() > 0) {
                // Utilizza un meccanismo di blocco o transazione per garantire l'atomicità dell'operazione
                synchronized (this) {
                    book1.setQuantity(book1.getQuantity() - 1);
                    bookRepository.save(book1);
                    orderBook.setQuantity(orderBook.getQuantity() + 1);
                    orderBook = orderBookRepository.save(orderBook);
                }
                return orderBook;
            } else {
                throw new CustomException("Il libro selezionato non è più disponibile in magazzino.");
            }
        }
    }

    @Transactional
    public Order checkout(String userId, OrderForm orderForm) {
        UserRepresentation userRepresentation = keycloakService.getUserById(userId).orElseThrow(
                () -> new CustomException("Utente non trovato."));
        Optional<User> userOptional = userRepository.findById(userId);

        Order pendingCart = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        if (orderForm.getRecipientName() == null ||
                orderForm.getShippingAddress() == null ||
                orderForm.getPhoneNumber() == null) {
            throw new CustomException("Errore durante il check-out. Campi richiesti mancanti.");
        }

        Order order = new Order();
        order.setId(pendingCart.getId());
        order.setUser(userOptional.get());
        order.setDateCreated(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setRecipientName(orderForm.getRecipientName());
        order.setShippingAddress(orderForm.getShippingAddress());
        order.setPhoneNumber(orderForm.getPhoneNumber());
        order.setTotalAmount(pendingCart.getTotalPrice());
        orderRepository.save(order);

        createNewPendingCartForUser(userRepresentation.getId());
        return order;
    }


    private void createNewPendingCartForUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        Order newPendingCart = new Order();
        newPendingCart.setUser(userOptional.get());
        newPendingCart.setDateCreated(LocalDateTime.now());
        newPendingCart.setTotalAmount(0D);
        newPendingCart.setOrderStatus(OrderStatus.PENDING);
        orderRepository.save(newPendingCart);
    }

    public List<Order> getAllOrdersOfUser(String userId) {
        return orderRepository.findAllByUserId(userId);
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderStatus();
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatusDTO orderStatus) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if(orderOptional.isPresent()) {
            Order updateOrder = orderOptional.get();
            updateOrder.setOrderStatus(orderStatus.getOrderStatus());
            return  orderRepository.save(updateOrder);
        }else{
            throw new CustomException("Impossibile aggiornare lo stato dell'ordine.");
        }
    }
}
