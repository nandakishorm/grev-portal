--DROP TABLE GREVPORTAL.dbo.GRVPubUser;
--DROP TABLE GREVPORTAL.dbo.GRVPubRole;
--DROP TABLE GREVPORTAL.dbo.GRVPubCategory;
--DROP TABLE GREVPORTAL.dbo.GRVPubPost;
--DROP TABLE GREVPORTAL.dbo.GRVPubPostImage;
--DROP TABLE GREVPORTAL.dbo.GRVPubEscalate;

SELECT * FROM GREVPORTAL.dbo.GRVPubUser ;
SELECT * FROM GREVPORTAL.dbo.GRVPubRole ;
SELECT * FROM GREVPORTAL.dbo.GRVPubCategory;
SELECT * FROM GREVPORTAL.dbo.GRVPubPost;
SELECT * FROM GREVPORTAL.dbo.GRVPubPostImage;
SELECT * FROM GREVPORTAL.dbo.GRVPubEscalate;

select count(1) from GREVPORTAL.dbo.GRVPubPost p where p.IsResolved=1 and p.ParentPostID is null;

select * from GREVPORTAL.dbo.GRVPubPost p where p.isResolved=1 and p.ParentPostID is null
and p.DateCreated between ('2023-12-03 04:31:11.770') and ('2023-12-06 04:31:11.770');

update dbo.GRVPubUser set rolename = 'ROLE_ADMIN' where id = 1;
delete from dbo.GRVPubUser where id = 5;
delete from dbo.GRVPubPost where id = 4;

------------------------------------------- CREATE TABLES-------------------------------------------------------------
CREATE TABLE GREVPORTAL.dbo.GRVPubUser(
ID bigint IDENTITY(1,1) NOT NULL,
UserID nvarchar(255) NOT NULL,
RoleName nvarchar(255) NOT NULL,
Username nvarchar(255) NOT NULL,
Password nvarchar(255) NOT NULL,
FirstName nvarchar(255) NOT NULL,
LastName nvarchar(255) NOT NULL,
Email nvarchar(255) NOT NULL,
Phone nvarchar(255) NOT NULL,
Active int DEFAULT 1 NULL,
DateCreated datetime DEFAULT getdate() NOT NULL,
UserCreated nvarchar(255) DEFAULT suser_sname() NOT NULL,
DateModified datetime NULL,
UserModified nvarchar(255) NULL,
);

CREATE TABLE GREVPORTAL.dbo.GRVPubRole(
ID bigint IDENTITY(1,1) NOT NULL,
RoleID nvarchar(255) NOT NULL,
RoleName nvarchar(255) NOT NULL,
Active int DEFAULT 1 NOT NULL,
DateCreated datetime DEFAULT getdate() NOT NULL,
UserCreated nvarchar(255) DEFAULT suser_sname() NOT NULL,
DateModified datetime NULL,
UserModified nvarchar(255) NULL,
);

CREATE TABLE GREVPORTAL.dbo.GRVPubCategory(
ID bigint IDENTITY(1,1) NOT NULL,
CategoryID nvarchar(255) NOT NULL,
CategoryName nvarchar(255) NOT NULL,
CategoryType nvarchar(255) DEFAULT 'General' NOT NULL,
Active int DEFAULT 1 NOT NULL,
DateCreated datetime DEFAULT getdate() NOT NULL,
UserCreated nvarchar(255) DEFAULT suser_sname() NOT NULL,
DateModified datetime NULL,
UserModified nvarchar(255) NULL,
);

CREATE TABLE GREVPORTAL.dbo.GRVPubPost(
ID bigint IDENTITY(1,1) NOT NULL,
UserID nvarchar(255) NOT NULL, --uuid
PostID nvarchar(255) NOT NULL, --uuid
ParentPostID nvarchar(255) NULL, --uuid
TicketID bigint NOT NULL, --number
Post nvarchar (MAX) NOT NULL,
CategoryID nvarchar(255) NOT NULL, --uuid
upVotesCount int DEFAULT 0 NULL,
isResolved int DEFAULT 0 NULL,
isEscalated int DEFAULT 0 NULL,
Active int DEFAULT 1 NOT NULL,
DateCreated datetime DEFAULT getdate() NOT NULL,
UserCreated nvarchar(255) DEFAULT suser_sname() NOT NULL,
DateModified datetime NULL,
UserModified nvarchar(255) NULL,
CONSTRAINT UK_PostID UNIQUE(PostID),
);

CREATE TABLE GREVPORTAL.dbo.GRVPubPostImage(
ID bigint IDENTITY(1,1) NOT NULL,
PostID nvarchar(255) NOT NULL, --uuid
FileObj varbinary(max) NOT NULL,
Active int DEFAULT 1 NOT NULL,
DateCreated datetime DEFAULT getdate() NOT NULL,
UserCreated nvarchar(255) DEFAULT suser_sname() NOT NULL,
DateModified datetime NULL,
UserModified nvarchar(255) NULL,
);

CREATE TABLE GREVPORTAL.dbo.GRVPubEscalate(
ID bigint IDENTITY(1,1) NOT NULL,
PostID nvarchar(255) NOT NULL, --uuid
UserID nvarchar(255) NOT NULL, --uuid
Active int DEFAULT 1 NOT NULL,
DateCreated datetime DEFAULT getdate() NOT NULL,
UserCreated nvarchar(255) DEFAULT suser_sname() NOT NULL,
DateModified datetime NULL,
UserModified nvarchar(255) NULL,
);