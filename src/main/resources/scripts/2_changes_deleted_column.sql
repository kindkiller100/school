alter table subjects add column deleted boolean default false;

alter table lessons drop column deleted;
