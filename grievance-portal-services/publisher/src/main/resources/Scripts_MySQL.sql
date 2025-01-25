------------------------------------------- CREATE TABLES-------------------------------------------------------------
CREATE TABLE GRVPubUser (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    UserID VARCHAR(255) NOT NULL,
    RoleName VARCHAR(255) NOT NULL,
    Username VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NOT NULL,
    Phone VARCHAR(255) NOT NULL,
    Active INT DEFAULT 1,
    DateCreated DATETIME DEFAULT CURRENT_TIMESTAMP,
    UserCreated VARCHAR(255) DEFAULT 'system',
    DateModified DATETIME,
    UserModified VARCHAR(255)
);

CREATE TABLE GRVPubRole (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    RoleID VARCHAR(255) NOT NULL,
    RoleName VARCHAR(255) NOT NULL,
    Active INT DEFAULT 1,
    DateCreated DATETIME DEFAULT CURRENT_TIMESTAMP,
    UserCreated VARCHAR(255) DEFAULT 'system',
    DateModified DATETIME,
    UserModified VARCHAR(255)
);

CREATE TABLE GRVPubCategory (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    CategoryID VARCHAR(255) NOT NULL,
    CategoryName VARCHAR(255) NOT NULL,
    CategoryType VARCHAR(255) DEFAULT 'General',
    Active INT DEFAULT 1,
    DateCreated DATETIME DEFAULT CURRENT_TIMESTAMP,
    UserCreated VARCHAR(255) DEFAULT 'system',
    DateModified DATETIME,
    UserModified VARCHAR(255)
);

CREATE TABLE GRVPubPost (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    UserID VARCHAR(255) NOT NULL,
    PostID VARCHAR(255) NOT NULL,
    ParentPostID VARCHAR(255),
    TicketID BIGINT NOT NULL,
    Post LONGTEXT NOT NULL,
    CategoryID VARCHAR(255) NOT NULL,
    upVotesCount INT DEFAULT 0,
    isResolved INT DEFAULT 0,
    isEscalated INT DEFAULT 0,
    Active INT DEFAULT 1,
    DateCreated DATETIME DEFAULT CURRENT_TIMESTAMP,
    UserCreated VARCHAR(255) DEFAULT 'system',
    DateModified DATETIME,
    UserModified VARCHAR(255),
    UNIQUE KEY UK_PostID (PostID)
);

CREATE TABLE GRVPubPostImage (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    PostID VARCHAR(255) NOT NULL,
    FileObj LONGBLOB NOT NULL,
    Active INT DEFAULT 1,
    DateCreated DATETIME DEFAULT CURRENT_TIMESTAMP,
    UserCreated VARCHAR(255) DEFAULT 'system',
    DateModified DATETIME,
    UserModified VARCHAR(255)
);

CREATE TABLE GRVPubEscalate (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    PostID VARCHAR(255) NOT NULL,
    UserID VARCHAR(255) NOT NULL,
    Active INT DEFAULT 1,
    DateCreated DATETIME DEFAULT CURRENT_TIMESTAMP,
    UserCreated VARCHAR(255) DEFAULT 'system',
    DateModified DATETIME,
    UserModified VARCHAR(255)
);

--------------------------------------------------------------DATA ENTRY-------------------------------------------------------
INSERT INTO GREVPORTAL.GRVPubUser
(ID, UserID, RoleName, Username, Password, FirstName, LastName, Email, Phone, Active, DateCreated, UserCreated, DateModified, UserModified)
VALUES(1, 'f81f478a-77ff-4d2d-835d-c73ea8ebdc86', 'ROLE_USER', 'nandakishor_bayar', '$2a$10$swP27Mbu34gD5WaeXkGvY.vpujblqUwrsEkeJsKwBk8pLuTzfSy02', 'Nanda', 'KishorM', 'nanda.bayar@gmail.com', '9988774455', 1, '2025-01-25 21:45:12', 'nandakishor', '2025-01-25 21:45:12', 'nandakishor');

update GRVPubUser set rolename = 'ROLE_ADMIN' where id = 1;