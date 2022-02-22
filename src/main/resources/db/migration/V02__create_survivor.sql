CREATE TABLE survivor(
    id UUID NOT NULL,
    name VARCHAR NOT NULL,
    age INTEGER NOT NULL,
    gender VARCHAR NOT NULL,
    infected BOOLEAN DEFAULT FALSE,
    longitude FLOAT NOT NULL,
    latitude FLOAT NOT NULL,
    PRIMARY KEY (id)
)