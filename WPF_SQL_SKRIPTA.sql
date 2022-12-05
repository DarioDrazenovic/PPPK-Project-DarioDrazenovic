/*---------------------PERSON---------------------------*/

CREATE TABLE Person
(
	IDPerson int primary key identity,
	FirstName nvarchar(20) not null,
	LastName nvarchar(20) not null,
	Age int not null,
	Email nvarchar(30) not null,
	Picture varbinary(max) null
)
GO

insert into Person (FirstName, LastName, Age, Email) values('dario', 'drazenovic', '21', 'dario.drazenovic@gmail.com')
go

CREATE PROC GetPeople
AS
SELECT * FROM Person
GO

CREATE PROC GetPerson
	@idPerson int
AS
SELECT * FROM Person WHERE IDPerson = @idPerson
GO

CREATE PROC DeletePerson
	@idPerson int
AS
DELETE FROM Person WHERE IDPerson = @idPerson
GO

CREATE PROC AddPerson
	@firstname nvarchar(20),
	@lastname nvarchar(20),
	@age int,
	@email nvarchar(30),
	@picture varbinary(max),
	@idPerson INT OUTPUT
AS 
BEGIN
INSERT INTO Person VALUES (@firstname, @lastname, @age, @email, @picture)
	SET @idPerson = SCOPE_IDENTITY()
END
GO

CREATE PROC UpdatePerson
	@firstname nvarchar(20),
	@lastname nvarchar(20),
	@age int,
	@email nvarchar(30),
	@picture varbinary(max),
	@idPerson int
AS
UPDATE Person SET 
		FirstName = @firstname,
		LastName = @lastname,
		Age = @age,
		Email = @email,
		Picture = @picture
	WHERE 
		IDPerson = @idPerson

/*--------------------Esports-----------------------------*/

CREATE TABLE EsportsTeam
(
	IDETeam int primary key identity,
	TeamName nvarchar(50) not null,
	Country nvarchar(50) not null,
	PersonID int constraint FK_EsportsTeam_Person foreign key references Person(IDPerson)
)
GO

CREATE PROC GetEsportsTeams
AS
SELECT * FROM EsportsTeam
GO

CREATE PROC GetEsportTeam
	@idETeam int
AS
SELECT * FROM EsportsTeam WHERE IDETeam = @idETeam
GO

CREATE PROC DeleteEsportsTeam
	@idETeam int
AS
DELETE FROM EsportsTeam WHERE IDETeam = @idETeam
GO

Alter PROC AddEsportsTeam
	@teamName nvarchar(20),
	@country nvarchar(20),
	@personid int,
	@idETeam INT OUTPUT
AS 
BEGIN
INSERT INTO EsportsTeam VALUES (@teamName, @country, @personid)
	SET @idETeam = SCOPE_IDENTITY()
END
GO

CREATE PROC UpdateEsportsTeam
	@teamName nvarchar(20),
	@country nvarchar(20),
	@personId int,
	@idETeam int
AS
UPDATE EsportsTeam SET 
		TeamName = @teamName,
		Country = @country,
		PersonID = @personId
	WHERE 
		IDETeam = @idETeam