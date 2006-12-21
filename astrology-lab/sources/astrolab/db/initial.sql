GRANT ALL PRIVILEGES ON *.* TO 'develop'@'localhost' IDENTIFIED BY 'develop' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'develop'@'%' IDENTIFIED BY 'develop' WITH GRANT OPTION;
GRANT RELOAD, PROCESS ON *.* TO 'develop'@'localhost';
GRANT USAGE ON *.* TO 'develop'@'localhost';

create table text (
  id INT UNSIGNED NOT NULL,
  accessible_by INT UNSIGNED,
  descrid TEXT,
  en TEXT,
  bg TEXT,

  PRIMARY KEY (id),
  INDEX (id)
) ENGINE=InnoDB DEFAULT CHARSET cp1251 COLLATE cp1251_bulgarian_ci;

create table svg (
  id INT UNSIGNED NOT NULL REFERENCES text (id),
  svg TEXT
) ENGINE=InnoDB;

create table types (
  element_id INT UNSIGNED NOT NULL REFERENCES text (id),
  type_id INT UNSIGNED NOT NULL REFERENCES text (id)
) ENGINE=InnoDB;

create table users (
  user_id INT UNSIGNED NOT NULL PRIMARY KEY REFERENCES text (id),
  user_language ENUM ('en', 'bg' ),
  email VARCHAR(50),
  invitation DATETIME
  
) ENGINE=InnoDB;

create table project (
  name INT UNSIGNED NOT NULL,
  laboratory INT UNSIGNED NOT NULL,
  type ENUM ('white', 'red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'black'),
  started DATETIME,
  icon LONGBLOB,
  description INT UNSIGNED NOT NULL,

  INDEX (name),
  CONSTRAINT `project_laboratory` FOREIGN KEY (laboratory) REFERENCES text (id),
  CONSTRAINT `project_name_resource` FOREIGN KEY (name) REFERENCES text (id),
  CONSTRAINT `project_description_resource` FOREIGN KEY (description) REFERENCES text (id)
) ENGINE=InnoDB;

create table locations (
  id INT UNSIGNED NOT NULL PRIMARY KEY REFERENCES text (id),
  region INT UNSIGNED NOT NULL REFERENCES text (id),
  longitude DOUBLE,
  lattitude DOUBLE,
  time_zone int,

  INDEX (id)
) ENGINE=InnoDB;

create table archive (
  event_id INT UNSIGNED NOT NULL PRIMARY KEY,
  subject_id INT UNSIGNED NOT NULL,
  event_time DATETIME,
  location INT UNSIGNED NOT NULL,
  type ENUM ('male', 'female', 'event', 'resource'),
  accuracy ENUM ('a second', 'a minute', '5 minutes', '10 minutes', '30 minutes', 'an hour', 'few hours', 'a day', 'a week', 'a month', 'an year'),
  source ENUM ('accurate', 'rectified', 'recalled', 'guessed', 'planned'),

  CONSTRAINT `archive_event_id` FOREIGN KEY (event_id) REFERENCES text (id),
  CONSTRAINT `archive_subject_id` FOREIGN KEY (subject_id) REFERENCES text (id),
  CONSTRAINT `archive_location` FOREIGN KEY (location) REFERENCES locations (id)
) ENGINE=InnoDB;

create table attribute (
  object_id INT UNSIGNED NOT NULL,
  attribute_id INT UNSIGNED NOT NULL,
  attribute_value DOUBLE,

  INDEX (object_id, attribute_id),
  CONSTRAINT `attribute_object_id` FOREIGN KEY (object_id) REFERENCES text (id),
  CONSTRAINT `attribute_attribute_id` FOREIGN KEY (attribute_id) REFERENCES text (id)
) ENGINE=InnoDB;

create table views (
  view INT UNSIGNED NOT NULL PRIMARY KEY REFERENCES text (id),
  request VARCHAR(100)
) ENGINE=InnoDB;

create table actions (
  id INT UNSIGNED NOT NULL REFERENCES text (id),
  action_group INT UNSIGNED REFERENCES text (id),
  required_selection INT UNSIGNED REFERENCES text (id),
  project INT UNSIGNED REFERENCES text (id),
  from_view VARCHAR(50) REFERENCES views (view),
  inject_sequence VARCHAR(50) REFERENCES views (view),
  to_view VARCHAR(50) NOT NULL REFERENCES views (view),
  icon BLOB
) ENGINE=InnoDB;

create table favourites (
  user_id INT UNSIGNED NOT NULL REFERENCES archive (event_id),
  view_id INT UNSIGNED REFERENCES views (view),
  object_id INT UNSIGNED NOT NULL REFERENCES text (id),
  order_at INT UNSIGNED NOT NULL,
  custom BOOLEAN NOT NULL
) ENGINE=InnoDB;

create table action_group (
  id INT UNSIGNED NOT NULL REFERENCES text (id),
  action_group INT UNSIGNED REFERENCES text (id)
) ENGINE=InnoDB;

create table help_feedback (
  id INT UNSIGNED NOT NULL REFERENCES text (id),
  user_id INT UNSIGNED NOT NULL REFERENCES text (id),
  approve ENUM ('yes', 'no')
) ENGINE=InnoDB;

create table help_project (
  comment_id INT UNSIGNED NOT NULL REFERENCES text (id),
  commenter_id INT UNSIGNED NOT NULL REFERENCES users (user_id),
  project_id INT UNSIGNED NOT NULL REFERENCES text (id),

  INDEX (comment_id)
) ENGINE=InnoDB;

create table formula_description (
  formulae_id INT UNSIGNED NOT NULL REFERENCES text (id),
  project_id INT UNSIGNED NOT NULL REFERENCES text (id),
  owner_id INT UNSIGNED NOT NULL REFERENCES text (id),
  score DOUBLE
) ENGINE=InnoDB;

create table formula_elements (
  formulae_id INT UNSIGNED NOT NULL REFERENCES formula_description (id),
  element_coefficient DOUBLE NOT NULL,
  element_id INT UNSIGNED NOT NULL REFERENCES text (id)
) ENGINE=InnoDB;

create table project_statistics_value (
  event_id INT UNSIGNED NOT NULL REFERENCES text (id),
  project_id INT UNSIGNED NOT NULL REFERENCES project (id),
  record_value DOUBLE NOT NULL
) ENGINE=InnoDB;

create table project_human_birth (
  event_id INT UNSIGNED NOT NULL REFERENCES archive (event_id),

  INDEX (event_id)
) ENGINE=InnoDB;

create table project_diary (
  event_id INT UNSIGNED NOT NULL REFERENCES archive (event_id),
  description TEXT,

  INDEX (event_id)
) ENGINE=InnoDB;

create table project_relocation (
  event_id INT UNSIGNED NOT NULL REFERENCES archive (event_id),

  INDEX (event_id)
) ENGINE=InnoDB;

create table project_test_psycho_reaction (
  event_id INT UNSIGNED NOT NULL REFERENCES text (id),
  sequence INT UNSIGNED,
  answer INT UNSIGNED,
  color INT UNSIGNED,
  elapsed INT UNSIGNED,
  mousex INT UNSIGNED,
  mousey INT UNSIGNED,

  INDEX (event_id)
) ENGINE=InnoDB;

create table project_test_psycho_color_blocks (
  event_id INT UNSIGNED NOT NULL REFERENCES text (id),
  sequence INT UNSIGNED,
  answer INT UNSIGNED,
  color INT UNSIGNED,
  elapsed INT UNSIGNED,
  mousex INT UNSIGNED,
  mousey INT UNSIGNED,

  INDEX (event_id)
) ENGINE=InnoDB;

create table project_financial_products (
  product_id INT UNSIGNED NOT NULL REFERENCES text (id),
  product_category INT UNSIGNED REFERENCES text (id),

  INDEX (product_id)
) ENGINE=InnoDB;

create table project_financial_balance (
  owner_id INT UNSIGNED REFERENCES archive (event_id),
  owner_operation ENUM ('buy', 'sell'),
  item_id INT UNSIGNED REFERENCES archive (event_id),
  item_type INT UNSIGNED NOT NULL REFERENCES project_financial_products (product_id),
  purchase_time DATETIME,
  purchase_price DOUBLE NOT NULL,
  price_currency ENUM ('BGN', 'EUR', 'USD'),
  purchase_quantity DOUBLE NOT NULL,
  purchase_measure ENUM ('item', 'kilogram', 'liter'),

  INDEX (owner_id)
) ENGINE=InnoDB;

create table project_sleep (
  sleep_from INT UNSIGNED NOT NULL REFERENCES archive (event_id),
  sleep_to INT UNSIGNED NOT NULL REFERENCES archive (event_id),

  INDEX (sleep_from),
  INDEX (sleep_to)
) ENGINE=InnoDB;

insert into text values (0, NULL, NULL, '... not set ...', '... липсва ...');

insert into text values (1000001, NULL, 'type', NULL, NULL);
insert into text values (1000002, NULL, 'planet', 'planet', 'планета');
insert into text values (1000003, NULL, 'house', 'house', 'дом');
insert into text values (1000004, NULL, 'formulae_element', 'formulae element', 'елемент за формула');

insert into text values (2000001, NULL, NULL, 'Stephan Zlatarev', 'Стефан Златарев');
insert into text values (2000002, NULL, NULL, 'Svetlana Stancheva', 'Светлана Станчева');
insert into text values (2000003, NULL, NULL, 'gufi', 'gufi');
insert into text values (2000004, NULL, NULL, 'Altrance', 'Altrance');

insert into text values (3000001, NULL, NULL, 'Archive', 'Архив');
insert into text values (3000002, NULL, NULL, 'Library', 'Библиотека');
insert into text values (3000003, NULL, NULL, 'Collection of natal and event charts', 'Колекция от рожденни дати и събития');
insert into text values (3000004, NULL, NULL, 'Psychometrica', 'Психометрика');
insert into text values (3000005, NULL, 'test_psycho_reaction', 'Reaction Test', 'Тест за реакция');
insert into text values (3000006, NULL, NULL, 'Test reaction speed', 'Тества реакцията');
insert into text values (3000007, NULL, 'test_psycho_color_blocks', 'Color Block Test', 'Тест с цветни блокчета');
insert into text values (3000008, NULL, NULL, 'Test speed of color combination', 'Тества скорост на тырсене на цветове');
insert into text values (3000009, NULL, NULL, 'Biology', 'Биология');
insert into text values (3000010, NULL, NULL, 'Moon Cycle', 'Лунен цикъл');
insert into text values (3000011, NULL, NULL, 'Women cycle research', 'Изследва женския цикъл');
insert into text values (3000012, NULL, NULL, 'Synastry', 'Синастрия');
insert into text values (3000013, NULL, NULL, 'Partner Match', 'Съвместимост между партньори');
insert into text values (3000014, NULL, NULL, 'Partner Match ...', 'Съвместимост между партньори ...');
insert into text values (3000015, NULL, NULL, 'Diary', 'Дневник');
insert into text values (3000016, NULL, NULL, 'Collection of personal event records', 'Записи на лични събития');
insert into text values (3000017, NULL, NULL, 'Relocations', 'Релокация');
insert into text values (3000018, NULL, NULL, 'Records of temporary and lasting personal relocations', 'Записи на промяна на местонахожденията');
insert into text values (3000019, NULL, NULL, 'Geography', 'География');
insert into text values (3000020, NULL, NULL, 'Records of populated places', 'Записи на населени места');
insert into text values (3000021, NULL, NULL, 'Finance', 'Финанси');
insert into text values (3000022, NULL, NULL, 'Balance', 'Баланс');
insert into text values (3000023, NULL, NULL, 'Products', 'Продукти');
insert into text values (3000024, NULL, NULL, 'Personal Office', 'Личен кабинет');
insert into text values (3000025, NULL, NULL, 'Sleep', 'Сън');
insert into text values (3000026, NULL, NULL, 'Astronomy', 'Астрономия');
insert into text values (3000027, NULL, NULL, 'Sun Spots', 'Слънчеви петна');

insert into text values (4000002, NULL, NULL, 'Description', 'Описание');
insert into text values (4000003, NULL, NULL, 'Chart', 'Карта');
insert into text values (4000004, NULL, NULL, 'Save', 'Запази');
insert into text values (4000005, NULL, NULL, 'Edit Time', 'Редактирай време');
insert into text values (4000006, NULL, NULL, 'Edit Location', 'Редактирай местоположение');
insert into text values (4000007, NULL, NULL, 'Position of Planets', 'Позиция на планети');
insert into text values (4000008, NULL, NULL, 'Position of Houses', 'Позиция на домове');
insert into text values (4000009, NULL, NULL, 'Track', 'Трасе');
insert into text values (4000011, NULL, NULL, 'Deselect', 'Махни селекция');
insert into text values (4000014, NULL, NULL, 'Laboratory', 'Лаборатория');
insert into text values (4000016, NULL, NULL, 'Project', 'Проект');
insert into text values (4000018, NULL, NULL, 'Enter data', 'Въведи данни');
insert into text values (4000019, NULL, NULL, 'Result', 'Резултат');
insert into text values (4000020, NULL, NULL, 'Tools', 'Инструменти');
insert into text values (4000021, NULL, NULL, 'Send feedback', 'Дай мнение');
insert into text values (4000022, NULL, NULL, 'Go to laboratory', 'Избери лаборатория');
insert into text values (4000023, NULL, NULL, 'Go to project', 'Избери проект');
insert into text values (4000024, NULL, NULL, 'Details', 'Детайли');
insert into text values (4000025, NULL, NULL, 'List records', 'Покажи записи');
insert into text values (4000026, NULL, NULL, 'View general power', 'Покажи обща сила');
insert into text values (4000027, NULL, NULL, 'View general sign match', 'Покажи обща съвместимост между знаците');
insert into text values (4000028, NULL, NULL, 'Help', 'Помощ');
insert into text values (4000029, NULL, NULL, 'View', 'Покажи');
insert into text values (4000030, NULL, NULL, 'Edit record', 'Редактирай записа');
insert into text values (4000031, NULL, NULL, 'Send invitation', 'Прати покана');
insert into text values (4000032, NULL, NULL, 'Display formula', 'Покажи формули');
insert into text values (4000033, NULL, NULL, 'Edit formulae', 'Редактирай формула');
insert into text values (4000034, NULL, NULL, 'Display formulae chart', 'Покажи показания на формулата');

insert into text values (5000001, NULL, NULL, 'Bulgaria', 'България');
insert into text values (5000002, NULL, NULL, 'Sofia', 'София');
insert into text values (5000003, NULL, NULL, 'Stara Zagora', 'Стара Загора');
insert into text values (5000004, NULL, NULL, 'Aytos', 'Айтос');
insert into text values (5000005, NULL, NULL, 'Varna', 'Варна');

insert into text values (6000001, NULL, NULL, 'Description', 'Описание');
insert into text values (6000002, NULL, NULL, 'Subject', 'Субект');
insert into text values (6000003, NULL, NULL, 'Location', 'Място');
insert into text values (6000004, NULL, NULL, 'Accuracy', 'Точност');
insert into text values (6000005, NULL, NULL, 'Source', 'Достоверност');
insert into text values (6000006, NULL, NULL, 'Time of occurance', 'Време на събитие');
insert into text values (6000007, NULL, NULL, 'Enter the description of the event here!', 'Въведи описанието тук!');
insert into text values (6000008, NULL, NULL, 'Report any different location in project Relocation!', 'Обяви място чрез проект Релокация!');
insert into text values (6000009, NULL, NULL, 'This is you.', 'Това си ти.');
insert into text values (6000010, NULL, NULL, 'Hello', 'Здравей');
insert into text values (6000011, NULL, NULL, 'Pro', 'За');
insert into text values (6000012, NULL, NULL, 'Con', 'Против');
insert into text values (6000013, NULL, NULL, 'Message', 'Съобщение');
insert into text values (6000014, NULL, NULL, 'Opinion', 'Мнение');
insert into text values (6000015, NULL, 'Sun', 'Sun', 'Слънце');
insert into text values (6000016, NULL, 'Moon', 'Moon', 'Луна');
insert into text values (6000017, NULL, 'Mercury', 'Mercury', 'Меркурий');
insert into text values (6000018, NULL, 'Venus', 'Venus', 'Венера');
insert into text values (6000019, NULL, 'Mars', 'Mars', 'Марс');
insert into text values (6000020, NULL, 'Jupiter', 'Jupiter', 'Юпитер');
insert into text values (6000021, NULL, 'Saturn', 'Saturn', 'Сатурн');
insert into text values (6000022, NULL, 'Uranus', 'Uranus', 'Уран');
insert into text values (6000023, NULL, 'Neptune', 'Neptune', 'Нептун');
insert into text values (6000024, NULL, 'Pluto', 'Pluto', 'Плутон');
insert into text values (6000025, NULL, NULL, 'male', 'мъж');
insert into text values (6000026, NULL, NULL, 'female', 'жена');
insert into text values (6000027, NULL, NULL, 'event', 'събитие');
insert into text values (6000028, NULL, NULL, 'a second', 'секунда');
insert into text values (6000029, NULL, NULL, 'a minute', 'минута');
insert into text values (6000030, NULL, NULL, '5 minutes', '5 минути');
insert into text values (6000031, NULL, NULL, '10 minutes', '10 минути');
insert into text values (6000032, NULL, NULL, '30 minutes', '30 минути');
insert into text values (6000033, NULL, NULL, 'an hour', 'час');
insert into text values (6000034, NULL, NULL, 'few hours', 'часове');
insert into text values (6000035, NULL, NULL, 'a day', 'ден');
insert into text values (6000036, NULL, NULL, 'a week', 'седмица');
insert into text values (6000037, NULL, NULL, 'a month', 'месец');
insert into text values (6000038, NULL, NULL, 'an year', 'година');
insert into text values (6000039, NULL, NULL, 'accurate', 'точно');
insert into text values (6000040, NULL, NULL, 'rectified', 'ректифицирано');
insert into text values (6000041, NULL, NULL, 'recalled', 'припомнено');
insert into text values (6000042, NULL, NULL, 'guessed', 'налучкано');
insert into text values (6000043, NULL, NULL, 'planned', 'планирано');
insert into text values (6000044, NULL, NULL, 'The Archive contains', 'Архивът съдържа');
insert into text values (6000045, NULL, NULL, 'records of births.', 'записа на рожденни дати');
insert into text values (6000046, NULL, NULL, 'Enter part of name or birthplace', 'Въведи част от име или място');
insert into text values (6000047, NULL, NULL, 'Search', 'Търси');
insert into text values (6000048, NULL, NULL, 'Your favourites are:', 'Често използваш:');
insert into text values (6000049, NULL, NULL, 'The laboratories are:', 'Лабораториите са:');
insert into text values (6000050, NULL, NULL, 'The projects are:', 'Проектите са:');
insert into text values (6000051, NULL, NULL, 'Product', 'Продукт');
insert into text values (6000052, NULL, NULL, 'Category', 'Категория');
insert into text values (6000053, NULL, NULL, 'Jan', 'Яну');
insert into text values (6000054, NULL, NULL, 'Feb', 'Фев');
insert into text values (6000055, NULL, NULL, 'Mar', 'Мар');
insert into text values (6000056, NULL, NULL, 'Apr', 'Апр');
insert into text values (6000057, NULL, NULL, 'May', 'Май');
insert into text values (6000058, NULL, NULL, 'Jun', 'Юни');
insert into text values (6000059, NULL, NULL, 'Jul', 'Юли');
insert into text values (6000060, NULL, NULL, 'Aug', 'Авг');
insert into text values (6000061, NULL, NULL, 'Sep', 'Сеп');
insert into text values (6000062, NULL, NULL, 'Oct', 'Окт');
insert into text values (6000063, NULL, NULL, 'Nov', 'Ное');
insert into text values (6000064, NULL, NULL, 'Dec', 'Дек');
insert into text values (6000065, NULL, NULL, 'kilogram', 'килограм');
insert into text values (6000066, NULL, NULL, 'liter', 'литър');
insert into text values (6000067, NULL, NULL, 'item', 'брой');
insert into text values (6000068, NULL, NULL, 'buy', 'купува');
insert into text values (6000069, NULL, NULL, 'sell', 'продава');
insert into text values (6000070, NULL, NULL, 'No opinions at the moment.', 'Няма мнения до момента.');
insert into text values (6000071, NULL, NULL, 'Please, select a project!', 'Моля, избери проект!');
insert into text values (6000072, NULL, NULL, 'Record is visible to', 'Записът е видим за');
insert into text values (6000073, NULL, NULL, 'everyone', 'всеки');
insert into text values (6000074, NULL, NULL, 'me only', 'мен само');
insert into text values (6000075, NULL, NULL, 'From', 'От');
insert into text values (6000076, NULL, NULL, 'To', 'До');
insert into text values (6000077, NULL, NULL, 'sleeps', 'спи');
insert into text values (6000078, NULL, 'Gamma', 'Gamma', 'Гама');

insert into text values (8000001, NULL, NULL, 'Food', 'Храна');
insert into text values (8000002, NULL, NULL, 'Drinks', 'Питиета');
insert into text values (8000003, NULL, NULL, 'Vegetables', 'Зеленчуци');
insert into text values (8000004, NULL, NULL, 'Fruits', 'Плодове');
insert into text values (8000005, NULL, NULL, 'Fast food', 'Бърза храна');
insert into text values (8000006, NULL, NULL, 'Nuts', 'Ядки');
insert into text values (8000007, NULL, NULL, 'Herbs', 'Билки');
insert into text values (8000008, NULL, NULL, 'Honey', 'Мед');
insert into text values (8000009, NULL, NULL, 'Mineral water', 'Минерална вода');
insert into text values (8000010, NULL, NULL, 'Carrots', 'Моркови');

insert into text values (10002001, NULL, NULL, 'subject', 'subject');
insert into text values (10002002, NULL, NULL, 'location', 'location');
insert into text values (10002003, NULL, NULL, 'time', 'time');
insert into text values (10002004, NULL, NULL, 'answer', 'answer');
insert into text values (10002005, NULL, NULL, 'color', 'color');
insert into text values (10002006, NULL, NULL, 'x', 'x');
insert into text values (10002007, NULL, NULL, 'y', 'y');
insert into text values (10002008, NULL, NULL, 'sequence', 'sequence');
insert into text values (10002009, NULL, 'menu.not-logged-in', 'You are not logged in!', 'Не си разпознат!');
insert into text values (10002010, NULL, 'subject.language', 'language', 'език');
insert into text values (10002011, NULL, 'subject.language.en', 'en', 'en');
insert into text values (10002012, NULL, 'subject.language.bg', 'bg', 'bg');
insert into text values (10002013, NULL, 'user.session.laboratory', 'lab', 'lab');
insert into text values (10002014, NULL, 'user.session.project', 'project', 'project');
insert into text values (10002015, NULL, 'menu.not-implemented', 'This page is not available yet!', 'Тази страница оше не е сьздадена!');
insert into text values (10002016, NULL, 'user.session.event.1', 'event1', 'event1');
insert into text values (10002017, NULL, 'user.session.event.2', 'event2', 'event2');
insert into text values (10002018, NULL, 'user.session.event.3', 'event3', 'event3');
insert into text values (10002019, NULL, 'user.session.event.4', 'event4', 'event4');
insert into text values (10002020, NULL, 'user.selection.x1', 'selection.x1', 'selection.x1');
insert into text values (10002021, NULL, 'user.selection.x2', 'selection.x2', 'selection.x2');
insert into text values (10002022, NULL, 'user.selection.y2', 'selection.y2', 'selection.y2');

insert into text values (10003001, NULL, 'Europe/Sofia', 'Europe/Sofia', 'Европа/София');

insert into svg values (6000015, "<svg:g style='stroke:gold;stroke-width:20;fill:none'><svg:circle r='90' /><svg:circle r='10' /></svg:g>");
insert into svg values (6000016, "<svg:path d='M-60 -80 A70 70 0 0 1 60 80 A40 70 -25 1 0 -60 -80' style='stroke:yellow;stroke-width:20;fill:none' />");
insert into svg values (6000017, "<svg:g style='stroke:yellow;stroke-width:20;fill:none'><svg:circle r='45' cy='-25' /><svg:path d='M-40 -90 A80 80 0 0 0 40 -90' /><svg:line y1='30' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (6000018, "<svg:g style='stroke:green;stroke-width:20;fill:none'><svg:circle r='60' cy='-30' /><svg:line y1='30' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (6000019, "<svg:g style='stroke:red;stroke-width:20;fill:none'><svg:circle r='70' cx='-15' cy='15' /><svg:line x1='40' y1='-40' x2='90' y2='-90' /><svg:path d='M90 -30 L90 -90 L30 -90' /></svg:g>");
insert into svg values (6000020, "<svg:g style='stroke:orange;stroke-width:20;fill:none'><svg:path d='M-90 -90 A70 70 0 0 1 -40 50 L90 50' /><svg:line x1='40' y1='10' x2='40' y2='90' /></svg:g>");
insert into svg values (6000021, "<svg:g style='stroke:darkolivegreen;stroke-width:20;fill:none'><svg:path d='M-20 -90 L-20 -20 A70 70 0 0 1 60 100' /><svg:line x1='-70' y1='-60' x2='20' y2='-60' /></svg:g>");
insert into svg values (6000022, "<svg:g style='stroke:cyan;stroke-width:20;fill:none'><svg:path d='M-90 -90 A50 50 0 1 1 -90 40' /><svg:path d='M90 -90 A50 50 0 1 0 90 40' /><svg:line x1='-40' y1='-30' x2='40' y2='-30' /><svg:line y1='-80' y2='30' /><svg:circle cy='65' r='25' /></svg:g>");
insert into svg values (6000023, "<svg:g style='stroke:teal;stroke-width:20;fill:none'><svg:path d='M-80 -90 A80 110 0 1 0 80 -90' /><svg:line y1='-80' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (6000024, "<svg:g style='stroke:firebrick;stroke-width:20;fill:none'><svg:circle cy='-60' r='30' /><svg:path d='M-80 -90 A80 110 0 1 0 80 -90' /><svg:line y1='20' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (6000078, "<svg:path d='M-40 90 L-40 -90 L40 -90' style='stroke:brown;stroke-width:20;fill:none' />");

insert into types values (6000015, 1000002);
insert into types values (6000015, 1000004);
insert into types values (6000016, 1000002);
insert into types values (6000016, 1000004);
insert into types values (6000017, 1000002);
insert into types values (6000017, 1000004);
insert into types values (6000018, 1000002);
insert into types values (6000018, 1000004);
insert into types values (6000019, 1000002);
insert into types values (6000019, 1000004);
insert into types values (6000020, 1000002);
insert into types values (6000020, 1000004);
insert into types values (6000021, 1000002);
insert into types values (6000021, 1000004);
insert into types values (6000022, 1000002);
insert into types values (6000022, 1000004);
insert into types values (6000023, 1000002);
insert into types values (6000023, 1000004);
insert into types values (6000024, 1000002);
insert into types values (6000024, 1000004);

insert into project values (3000001, 3000002, 'white', now(), NULL, 3000003);
insert into project values (3000005, 3000004, 'white', now(), NULL, 3000006);
insert into project values (3000007, 3000004, 'white', now(), NULL, 3000008);
insert into project values (3000010, 3000009, 'white', now(), NULL, 3000011);
insert into project values (3000013, 3000012, 'white', now(), NULL, 3000014);
insert into project values (3000015, 3000024, 'white', now(), NULL, 3000016);
insert into project values (3000017, 3000024, 'white', now(), NULL, 3000018);
insert into project values (3000019, 3000002, 'white', now(), NULL, 3000020);
insert into project values (3000022, 3000021, 'white', now(), NULL, 0);
insert into project values (3000023, 3000021, 'white', now(), NULL, 0);
insert into project values (3000025, 3000009, 'white', now(), NULL, 0);
insert into project values (3000027, 3000026, 'white', now(), NULL, 0);

insert into locations values (0, 0, 0, 0, 0);
insert into locations values (5000001,0,0,0,10003001);
insert into locations values (5000002,5000001,-23.3167,42.67,10003001);
insert into locations values (5000003,5000001,-25.634,42.4167,10003001);
insert into locations values (5000004,5000001,-27.25,42.7,10003001);
insert into locations values (5000005,5000001,-27.9167,43.2167,10003001);

insert into archive values (2000001, 2000001, '1979-01-09 01:30:00', 5000003, 'male', '10 minutes', 'accurate');
insert into archive values (2000002, 2000002, '1979-04-01 00:00:00', 5000004, 'female', '10 minutes', 'accurate');
insert into archive values (2000003, 2000003, '1986-11-23 18:15:00', 5000002, 'female', '10 minutes', 'accurate');
insert into archive values (2000004, 2000004, '1984-11-23 11:34:00', 5000005, 'male', '10 minutes', 'accurate');

insert into project_human_birth values (2000001);
insert into project_human_birth values (2000002);
insert into project_human_birth values (2000003);
insert into project_human_birth values (2000004);

INSERT INTO users VALUES (2000001, 'en', 'stephan.zlatarev@gmail.com', NULL);

insert into attribute values (2000001, 10002001, 2000001);
insert into attribute values (2000001, 10002002, 5000003);
insert into attribute values (2000001, 10002010, 10002012);
insert into attribute values (2000002, 10002001, 2000002);
insert into attribute values (2000002, 10002002, 5000004);


insert into views values (0, 'astrolab.web.project.archive.natal.DisplayNatalRecords');
insert into views values (2, 'astrolab.web.project.archive.TransformEventView');
insert into views values (3, 'astrolab.web.project.archive.natal.FormCreateNatalRecord');
insert into views values (4, 'astrolab.web.component.chart.Chart');
insert into views values (5, 'astrolab.web.project.archive.natal.ModifyCreateNatalRecord');
insert into views values (6, 'astrolab.web.project.archive.location.TransformEventEditTime');
insert into views values (7, 'astrolab.web.project.archive.location.TransformEventSaveTime');
insert into views values (8, 'astrolab.web.project.archive.location.TransformEventEditLocation');
insert into views values (9, 'astrolab.web.project.archive.location.TransformEventSaveLocation');
insert into views values (10, 'astrolab.web.component.chart.TablePlanets');
insert into views values (11, 'astrolab.web.component.chart.TableHouses');
insert into views values (12, 'astrolab.web.component.chart.Track');
insert into views values (13, 'astrolab.web.project.archive.test.Test1');
insert into views values (15, 'astrolab.web.project.archive.test.StoreTestResult');
insert into views values (17, 'astrolab.web.project.archive.SelectEvent');
insert into views values (18, 'astrolab.web.component.SelectAttribute');
insert into views values (19, 'astrolab.web.project.archive.test.Test2');
insert into views values (20, 'astrolab.web.project.archive.test.Test2Statistics');
insert into views values (21, 'astrolab.web.project.labs.LaboratoryList');
insert into views values (22, 'astrolab.web.project.labs.SelectLaboratory');
insert into views values (23, 'astrolab.web.project.labs.ProjectList');
insert into views values (24, 'astrolab.web.project.labs.SelectProject');
insert into views values (25, 'astrolab.web.component.general.PageNotImplemented');
insert into views values (26, 'astrolab.project.match.GeneralPowerView');
insert into views values (27, 'astrolab.project.match.GeneralSignMatchView');
insert into views values (28, 'astrolab.web.project.archive.diary.FormDiaryRecord');
insert into views values (29, 'astrolab.web.project.archive.diary.ModifyDiaryRecord');
insert into views values (30, 'astrolab.web.project.archive.relocation.DisplayRelocationRecords');
insert into views values (31, 'astrolab.web.project.archive.relocation.FormRelocationRecord');
insert into views values (32, 'astrolab.web.project.archive.relocation.ModifyRelocationRecord');
insert into views values (33, 'astrolab.web.component.location.DisplayLocationList');
insert into views values (34, 'astrolab.project.match.DisplayPartnerMatch');
insert into views values (35, 'astrolab.web.component.help.FormGiveFeedback');
insert into views values (36, 'astrolab.web.project.archive.natal.FormEditNatalRecord');
insert into views values (37, 'astrolab.web.component.location.FormAddLocation');
insert into views values (38, 'astrolab.web.component.location.ModifyLocation');
insert into views values (39, 'astrolab.web.project.finance.products.DisplayProductList');
insert into views values (40, 'astrolab.web.project.finance.products.FormAddProduct');
insert into views values (41, 'astrolab.web.project.finance.products.ModifyProduct');
insert into views values (42, 'astrolab.web.project.finance.balance.DisplayPurchaseList');
insert into views values (43, 'astrolab.web.project.finance.balance.FormAddPurchase');
insert into views values (44, 'astrolab.web.project.finance.balance.ModifyPurchase');
insert into views values (45, 'astrolab.web.component.help.DisplayProjectDescription');
insert into views values (46, 'astrolab.web.component.help.FormSendInvitation');
insert into views values (47, 'astrolab.web.component.help.ModifySendInvitation');
insert into views values (48, 'astrolab.project.sleep.FormSleepRecord');
insert into views values (49, 'astrolab.project.sleep.ModifySleepRecord');
insert into views values (50, 'astrolab.project.sleep.DisplaySleepRecords');
insert into views values (51, 'astrolab.project.statistics.ComponentChartStatistics');
insert into views values (52, 'astrolab.formula.display.DisplayProjectFormula');
insert into views values (53, 'astrolab.formula.display.ModifyFormulae');
insert into views values (54, 'astrolab.formula.display.FormEditFormulae');
insert into views values (55, 'astrolab.formula.display.ComponentChartFormulae');
insert into views values (56, 'astrolab.web.entrance.DisplayPersonalData');

insert into actions values (4000002, 4000016, NULL, NULL, NULL, NULL, 45, NULL);
insert into actions values (4000003, 4000020, 10002016, NULL, NULL, NULL, 4, NULL);
insert into actions values (4000004, NULL, NULL, NULL, 3, 5, 2, NULL);
insert into actions values (4000004, NULL, NULL, NULL, 36, 5, 2, NULL);
insert into actions values (4000004, NULL, NULL, NULL, 6, 7, 2, NULL);
insert into actions values (4000004, NULL, 10002016, NULL, 8, 9, 2, NULL);
insert into actions values (4000004, NULL, NULL, NULL, 28, 29, 2, NULL);
insert into actions values (4000004, NULL, NULL, NULL, 31, 32, 30, NULL);
insert into actions values (4000004, NULL, NULL, NULL, 37, 38, 33, NULL);
insert into actions values (4000004, NULL, NULL, NULL, 40, 41, 39, NULL);
insert into actions values (4000004, NULL, NULL, NULL, 43, 44, 42, NULL);
insert into actions values (4000004, NULL, NULL, NULL, 48, 49, 50, NULL);
insert into actions values (4000004, NULL, NULL, NULL, 54, 53, 52, NULL);
insert into actions values (4000005, NULL, NULL, NULL, 2, NULL, 6, NULL);
insert into actions values (4000006, NULL, NULL, NULL, 2, NULL, 8, NULL);
insert into actions values (4000007, 4000029, 10002016, NULL, NULL, NULL, 10, NULL);
insert into actions values (4000008, 4000029, 10002016, NULL, NULL, NULL, 11, NULL);
insert into actions values (4000009, NULL, 10002016, NULL, NULL, NULL, 12, NULL);
insert into actions values (4000011, NULL, 10002016, NULL, 0, 17, 0, NULL);
insert into actions values (4000011, NULL, 10002017, NULL, 2, 18, 2, NULL);
insert into actions values (4000011, NULL, 10002016, NULL, 30, 17, 30, NULL);
insert into actions values (4000018, 4000016, NULL, 3000007, NULL, 15, 13, NULL);
insert into actions values (4000018, 4000016, NULL, 3000005, NULL, 15, 19, NULL);
insert into actions values (4000018, 4000016, NULL, 3000001, NULL, NULL, 3, NULL);
insert into actions values (4000018, 4000016, NULL, 3000015, NULL, NULL, 28, NULL);
insert into actions values (4000018, 4000016, NULL, 3000017, NULL, NULL, 31, NULL);
insert into actions values (4000018, 4000016, NULL, 3000019, NULL, NULL, 37, NULL);
insert into actions values (4000018, 4000016, NULL, 3000022, NULL, NULL, 43, NULL);
insert into actions values (4000018, 4000016, NULL, 3000023, NULL, NULL, 40, NULL);
insert into actions values (4000018, 4000016, NULL, 3000025, NULL, NULL, 48, NULL);
insert into actions values (4000019, 4000016, NULL, 3000013, NULL, NULL, 34, NULL);
insert into actions values (4000021, 4000028, NULL, NULL, NULL, NULL, 35, NULL);
insert into actions values (4000023, 4000016, NULL, NULL, NULL, 24, 23, NULL);
insert into actions values (4000024, 4000029, NULL, NULL, NULL, NULL, 2, NULL);
insert into actions values (4000025, 4000016, NULL, 3000001, NULL, NULL, 0, NULL);
insert into actions values (4000025, 4000016, NULL, 3000017, NULL, NULL, 30, NULL);
insert into actions values (4000025, 4000016, NULL, 3000019, NULL, NULL, 33, NULL);
insert into actions values (4000025, 4000016, NULL, 3000022, NULL, NULL, 42, NULL);
insert into actions values (4000025, 4000016, NULL, 3000023, NULL, NULL, 39, NULL);
insert into actions values (4000025, 4000016, NULL, 3000027, NULL, NULL, 51, NULL);
insert into actions values (4000026, 4000016, NULL, 3000013, NULL, NULL, 26, NULL);
insert into actions values (4000027, 4000016, NULL, 3000013, NULL, NULL, 27, NULL);
insert into actions values (4000030, 4000016, NULL, 3000001, NULL, NULL, 36, NULL);
insert into actions values (4000031, 4000028, NULL, NULL, NULL, NULL, 46, NULL);
insert into actions values (4000031, 4000028, NULL, NULL, 46, 47, 46, NULL);
insert into actions values (4000032, 4000016, NULL, NULL, NULL, NULL, 52, NULL);
insert into actions values (4000033, 4000016, NULL, NULL, NULL, NULL, 54, NULL);
insert into actions values (4000034, 4000016, NULL, NULL, NULL, NULL, 55, NULL);

insert into action_group values (4000016, NULL);
insert into action_group values (4000020, NULL);
insert into action_group values (4000028, NULL);
insert into action_group values (4000029, 4000020);

insert into favourites values (2000001, 0, 2000001, 1, true);
insert into favourites values (2000001, 0, 2000002, 2, true);
insert into favourites values (2000001, 0, 2000003, 3, true);
insert into favourites values (2000001, 0, 2000004, 4, true);

insert into project_financial_products values (8000001, 0);
insert into project_financial_products values (8000002, 8000001);
insert into project_financial_products values (8000003, 8000001);
insert into project_financial_products values (8000004, 8000001);
insert into project_financial_products values (8000005, 8000001);
insert into project_financial_products values (8000006, 8000001);
insert into project_financial_products values (8000007, 8000001);
insert into project_financial_products values (8000008, 8000001);
insert into project_financial_products values (8000009, 8000002);
insert into project_financial_products values (8000010, 8000003);
