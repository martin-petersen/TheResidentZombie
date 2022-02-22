CREATE TABLE report(
   id UUID NOT NULL,
   infected_id UUID NOT NULL,
   reporter_id UUID NOT NULL,
   PRIMARY KEY (id),
   CONSTRAINT fk_infected FOREIGN KEY(infected_id) REFERENCES survivor(id),
   CONSTRAINT fk_reporter FOREIGN KEY(reporter_id) REFERENCES survivor(id)
)