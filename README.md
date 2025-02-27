## 1. Starting Keycloak Manually
1. From a terminal, open the keycloak-2x.x.x directory.

2. Enter the following command:

On Linux, run:
```
bin/kc.sh start-dev
```
On Windows, run:
```
bin\kc.bat start-dev
```
Using the start-dev option, you are starting Keycloak in development mode. In this mode, you can try out Keycloak for the first time to get it up and running quickly. This mode offers convenient defaults for developers, such as for developing a new Keycloak theme.

3. Access Keycloak Admin Console at ```http://localhost:8080/admin```.
    username: keycloak <br/>
    password: MyPassword$
    
## 2. Running the Backend (Spring Boot)
1. Navigate to the Spring Boot project Backend directory: 
```
cd PSW_Springboot_Angular_project/ecommerce-backend
```

2. Run:
```
mvn clean install
mvn spring-boot:run
```
## 3. Run the Frontend (Angular)
1. Navigate to the Spring Boot project Frontend directory: 
```
cd PSW_Springboot_Angular_project/ecommerce-frontend
```
2. Run
```
npm run dev
```

