insert into songs(title, artist, label, released, imageUrl)
values ('MacArthur Park', 'Richard Harris', 'Dunhill Records', 1968, 'https://picsum.photos/id/85/40/40'),
       ('Afternoon Delight', 'Starland Vocal Band', 'Windsong', 1976, 'https://picsum.photos/id/951/40/40'),
       ('Muskrat Love', 'Captain and Tennille', 'A&M', 1976, 'https://picsum.photos/id/861/40/40'),
       ('Sussudio', 'Phil Collins', 'Virgin', 1985, 'https://picsum.photos/id/912/40/40'),
       ('We Built This City', 'Starship', 'Grunt/RCA', 1985, 'https://picsum.photos/id/855/40/40'),
       ('Achy Breaky Heart', 'Billy Ray Cyrus', 'PolyGram Mercury', 1992, 'https://picsum.photos/id/795/40/40'),
       ('What’s Up?', '4 Non Blondes', 'Interscope', 1993, 'https://picsum.photos/id/824/40/40'),
       ('Who Let the Dogs Out?', 'Baha Men', 'S-Curve', 2000, 'https://picsum.photos/id/859/40/40'),
       ('My Humps', 'Black Eyed Peas', 'Universal Music', 2005, 'https://picsum.photos/id/90/40/40'),
       ('Chinese Food', 'Alison Gold', 'PMW Live', 2013, 'https://picsum.photos/id/874/40/40');

insert into songlist(ownerid, name, isprivate, imgUrl)
values ('eschuler', 'ElenasPublic', false, 'https://picsum.photos/id/224/600/600'),
       ('eschuler', 'ElenasPrivate', true, 'https://picsum.photos/id/137/600/600'),
       ('eschuler', 'ElenasPrivate', true, 'https://picsum.photos/id/145/600/600'),
       ('mmuster', 'MaximePrivate', true, 'https://picsum.photos/id/200/600/600'),
       ('mmuster', 'MaximePrivate', true, 'https://picsum.photos/id/855/600/600'),
       ('mmuster', 'MaximePublic', false, 'https://picsum.photos/id/836/600/600');

insert into songs_songlists(songlists_id, songs_id)
values (1, 1),
       (1, 4),
       (1, 5),
       (2, 4),
       (2, 6),
       (3, 3),
       (3, 4),
       (3, 6),
       (4, 8),
       (4, 9),
        (5, 3),
        (5, 4),
       (6, 2),
       (6,7);