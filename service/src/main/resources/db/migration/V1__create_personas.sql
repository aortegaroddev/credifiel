CREATE TABLE personas (
    id BIGINT IDENTITY(1,1) NOT NULL,
    nombre NVARCHAR(100) NOT NULL,
    edad INT NOT NULL,
    CONSTRAINT PK_personas PRIMARY KEY (id),
    CONSTRAINT CK_personas_edad CHECK (edad BETWEEN 0 AND 150)
);
