CREATE TABLE inventory(
     id UUID NOT NULL,
     amount INTEGER NOT NULL,
     item_id UUID NOT NULL,
     survivor_id UUID NOT NULL,
     PRIMARY KEY (id),
     CONSTRAINT fk_survivor FOREIGN KEY(survivor_id) REFERENCES survivor(id),
     CONSTRAINT fk_item FOREIGN KEY(item_id) REFERENCES item(id)
)