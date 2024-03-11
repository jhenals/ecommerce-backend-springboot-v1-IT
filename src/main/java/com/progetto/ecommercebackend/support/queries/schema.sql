#########################################################################################
###                                                                                   ###
### Author: Jhenalyn Subol                                                            ###
### License:                                                                          ###
### Date:                                                                             ###
### Version: 1.0                                                                      ###
###                                                                                   ###
#########################################################################################

CREATE SCHEMA IF NOT EXISTS ecommercedb;

use ecommercedb;

DROP TABLE IF EXISTS book;
create table ecommercedb.book
(
    id               bigint auto_increment
        primary key,
    cover_url        varchar(255) null,
    discount         int          null,
    price            double       null,
    publication_date date         null,
    title            varchar(255) null,
    category_id      bigint       null,
    constraint FKam9riv8y6rjwkua1gapdfew4j
        foreign key (category_id) references category (id)
);

DROP TABLE IF EXISTS author;
create table ecommercedb.author
(
    id   bigint auto_increment
        primary key,
    name varchar(255) null
);

DROP TABLE IF EXISTS book_author;
CREATE TABLE ecommercedb.book_author
(
    book_id   BIGINT NULL,
    author_id BIGINT NULL
);

ALTER TABLE book_author
    ADD CONSTRAINT FK_BOOK_AUTHOR_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES author (id);

ALTER TABLE book_author
    ADD CONSTRAINT FK_BOOK_AUTHOR_ON_BOOK FOREIGN KEY (book_id) REFERENCES book (id);
CREATE TABLE author
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NULL,
    CONSTRAINT PK_AUTHOR PRIMARY KEY (id)
);

CREATE TABLE book
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    cover_url        VARCHAR(255)          NULL,
    discount         INT                   NULL,
    price            DOUBLE                NULL,
    publication_date date                  NULL,
    title            VARCHAR(255)          NULL,
    category_id      BIGINT                NULL,
    CONSTRAINT PK_BOOK PRIMARY KEY (id)
);

CREATE TABLE book_author
(
    book_id   BIGINT NULL,
    author_id BIGINT NULL
);

CREATE TABLE category
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    name  VARCHAR(255)          NULL,
    value VARCHAR(255)          NULL,
    CONSTRAINT PK_CATEGORY PRIMARY KEY (id)
);

CREATE TABLE user
(
    username    VARCHAR(16)             NOT NULL,
    email       VARCHAR(255)            NULL,
    password    VARCHAR(32)             NOT NULL,
    create_time timestamp DEFAULT NOW() NULL
);

CREATE INDEX FK_BOOK_AUTHOR_ON_AUTHOR ON book_author (author_id);

CREATE INDEX FK_BOOK_AUTHOR_ON_BOOK ON book_author (book_id);

CREATE INDEX FKam9riv8y6rjwkua1gapdfew4j ON book (category_id);

ALTER TABLE book_author
    ADD CONSTRAINT FK_BOOK_AUTHOR_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES author (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE book_author
    ADD CONSTRAINT FK_BOOK_AUTHOR_ON_BOOK FOREIGN KEY (book_id) REFERENCES book (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE book
    ADD CONSTRAINT FKam9riv8y6rjwkua1gapdfew4j FOREIGN KEY (category_id) REFERENCES category (id) ON UPDATE RESTRICT ON DELETE RESTRICT;