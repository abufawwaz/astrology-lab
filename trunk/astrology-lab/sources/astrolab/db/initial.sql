GRANT ALL PRIVILEGES ON *.* TO 'develop'@'localhost' IDENTIFIED BY 'develop' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'develop'@'%' IDENTIFIED BY 'develop' WITH GRANT OPTION;
GRANT RELOAD, PROCESS ON *.* TO 'develop'@'localhost';
GRANT USAGE ON *.* TO 'develop'@'localhost';

create table text (
  id INT UNSIGNED UNIQUE NOT NULL,
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

create table views_perspective (
  perspective_id INT UNSIGNED NOT NULL REFERENCES text (id),
  perspective_html VARCHAR(2048) NOT NULL
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
  user_id INT UNSIGNED NOT NULL REFERENCES text (id),
  view_id INT UNSIGNED REFERENCES views (view),
  object_id INT UNSIGNED NOT NULL REFERENCES text (id),
  order_at INT UNSIGNED NOT NULL,
  custom BOOLEAN NOT NULL
) ENGINE=InnoDB;

create table help_project (
  comment_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  commenter_id INT UNSIGNED NOT NULL REFERENCES users (user_id),
  project_id INT UNSIGNED NOT NULL REFERENCES text (id),
  comment_text TEXT NOT NULL,

  INDEX (comment_id)
) ENGINE=InnoDB;

create table help_feedback (
  id INT UNSIGNED NOT NULL REFERENCES help_project (comment_id),
  user_id INT UNSIGNED NOT NULL REFERENCES users (user_id),
  approve ENUM ('yes', 'no')
) ENGINE=InnoDB;

create table formula (
  formulae_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  owner_id INT UNSIGNED NOT NULL REFERENCES text (id),
  formulae TEXT,

  PRIMARY KEY (formulae_id)
) ENGINE=InnoDB;

create table formula_chart (
  user_id INT UNSIGNED NOT NULL REFERENCES text (id),
  project_id INT UNSIGNED NOT NULL REFERENCES text (id),
  formulae_id INT UNSIGNED NOT NULL REFERENCES formula (formulae_id),
  score DOUBLE,
  chart_color ENUM ('red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'black')
) ENGINE=InnoDB;

create table formula_chart_base (
  user_id INT UNSIGNED NOT NULL REFERENCES text (id),
  project_id INT UNSIGNED NOT NULL REFERENCES text (id),
  base_id INT UNSIGNED NOT NULL REFERENCES formula (formulae_id),
  period_id INT UNSIGNED NOT NULL REFERENCES formula (formulae_id),
  from_time DATETIME,
  to_time DATETIME,
  selected BOOLEAN NOT NULL
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

create table project_geography (
  id INT UNSIGNED NOT NULL PRIMARY KEY REFERENCES text (id),
  region INT UNSIGNED NOT NULL REFERENCES text (id),
  longitude DOUBLE,
  lattitude DOUBLE,
  time_zone int,

  INDEX (id)
) ENGINE=InnoDB;

create table project_archive (
  event_id INT UNSIGNED NOT NULL UNIQUE PRIMARY KEY REFERENCES text (id),
  subject_id INT UNSIGNED NOT NULL REFERENCES text (id),
  event_time DATETIME,
  location INT UNSIGNED NOT NULL REFERENCES project_geography (id),
  type ENUM ('male', 'female', 'event', 'resource'),
  accuracy ENUM ('a second', 'a minute', '5 minutes', '10 minutes', '30 minutes', 'an hour', 'few hours', 'a day', 'a week', 'a month', 'an year'),
  source ENUM ('accurate', 'rectified', 'recalled', 'guessed', 'planned'),

  INDEX (event_id)
) ENGINE=InnoDB;

create table project_relocation (
  subject_id INT UNSIGNED NOT NULL REFERENCES project_archive (subject_id),
  event_time DATETIME,
  location INT UNSIGNED NOT NULL REFERENCES project_geography (id),

  INDEX (subject_id)
) ENGINE=InnoDB;

create table project_sunspots (
  time DATETIME,
  sunspots INT,

  INDEX (time)
) ENGINE=InnoDB;

insert into text values (0, NULL, NULL, '... not set ...', '... липсва ...');

insert into text values (1001, NULL, NULL, 'Jan', 'Яну');
insert into text values (1002, NULL, NULL, 'Feb', 'Фев');
insert into text values (1003, NULL, NULL, 'Mar', 'Мар');
insert into text values (1004, NULL, NULL, 'Apr', 'Апр');
insert into text values (1005, NULL, NULL, 'May', 'Май');
insert into text values (1006, NULL, NULL, 'Jun', 'Юни');
insert into text values (1007, NULL, NULL, 'Jul', 'Юли');
insert into text values (1008, NULL, NULL, 'Aug', 'Авг');
insert into text values (1009, NULL, NULL, 'Sep', 'Сеп');
insert into text values (1010, NULL, NULL, 'Oct', 'Окт');
insert into text values (1011, NULL, NULL, 'Nov', 'Ное');
insert into text values (1012, NULL, NULL, 'Dec', 'Дек');

insert into text values (2001, NULL, 'Aries', 'Aries', 'Овен');
insert into text values (2002, NULL, 'Taurus', 'Taurus', 'Телец');
insert into text values (2003, NULL, 'Gemini', 'Gemini', 'Близнаци');
insert into text values (2004, NULL, 'Cancer', 'Cancer', 'Рак');
insert into text values (2005, NULL, 'Leo', 'Leo', 'Лъв');
insert into text values (2006, NULL, 'Virgo', 'Virgo', 'Дева');
insert into text values (2007, NULL, 'Libra', 'Libra', 'Везни');
insert into text values (2008, NULL, 'Scorpio', 'Scorpio', 'Скорпион');
insert into text values (2009, NULL, 'Sagittarius', 'Sagittarius', 'Стрелец');
insert into text values (2010, NULL, 'Capricorn', 'Capricorn', 'Козирог');
insert into text values (2011, NULL, 'Aquarius', 'Aquarius', 'Водолей');
insert into text values (2012, NULL, 'Piesces', 'Piesces', 'Риби');
insert into text values (2101, NULL, 'Sun', 'Sun', 'Слънце');
insert into text values (2102, NULL, 'Moon', 'Moon', 'Луна');
insert into text values (2103, NULL, 'Mercury', 'Mercury', 'Меркурий');
insert into text values (2104, NULL, 'Venus', 'Venus', 'Венера');
insert into text values (2105, NULL, 'Mars', 'Mars', 'Марс');
insert into text values (2106, NULL, 'Jupiter', 'Jupiter', 'Юпитер');
insert into text values (2107, NULL, 'Saturn', 'Saturn', 'Сатурн');
insert into text values (2108, NULL, 'Uranus', 'Uranus', 'Уран');
insert into text values (2109, NULL, 'Neptune', 'Neptune', 'Нептун');
insert into text values (2110, NULL, 'Pluto', 'Pluto', 'Плутон');
insert into text values (2201, NULL, 'Gamma', 'Gamma', 'Гама');

insert into text values (3001, NULL, 'red', 'red', 'red');
insert into text values (3002, NULL, 'orange', 'orange', 'orange');
insert into text values (3003, NULL, 'yellow', 'yellow', 'yellow');
insert into text values (3004, NULL, 'green', 'green', 'green');
insert into text values (3005, NULL, 'blue', 'blue', 'blue');
insert into text values (3006, NULL, 'indigo', 'indigo', 'indigo');
insert into text values (3007, NULL, 'black', 'black', 'black');
insert into text values (3008, NULL, 'white', 'white', 'white');
insert into text values (3101, NULL, 'checked', 'checked', 'отбелязано');
insert into text values (3102, NULL, 'unchecked', 'unchecked', 'неотбелязано');

insert into text values (4001, NULL, NULL, 'subject', 'subject');
insert into text values (4002, NULL, NULL, 'location', 'location');
insert into text values (4003, NULL, NULL, 'time', 'time');
insert into text values (4004, NULL, NULL, 'answer', 'answer');
insert into text values (4005, NULL, NULL, 'color', 'color');
insert into text values (4006, NULL, NULL, 'x', 'x');
insert into text values (4007, NULL, NULL, 'y', 'y');
insert into text values (4008, NULL, NULL, 'sequence', 'sequence');
insert into text values (4009, NULL, 'menu.not-logged-in', 'You are not logged in!', 'Не си разпознат!');
insert into text values (4010, NULL, 'subject.language', 'language', 'език');
insert into text values (4011, NULL, 'subject.language.en', 'en', 'en');
insert into text values (4012, NULL, 'subject.language.bg', 'bg', 'bg');
insert into text values (4013, NULL, 'user.session.laboratory', 'lab', 'lab');
insert into text values (4014, NULL, 'user.session.project', 'project', 'project');
insert into text values (4015, NULL, 'menu.not-implemented', 'This page is not available yet!', 'Тази страница оше не е сьздадена!');
insert into text values (4016, NULL, 'user.session.event.1', 'event1', 'event1');
insert into text values (4017, NULL, 'user.session.event.2', 'event2', 'event2');
insert into text values (4018, NULL, 'user.session.event.3', 'event3', 'event3');
insert into text values (4019, NULL, 'user.session.event.4', 'event4', 'event4');
insert into text values (4020, NULL, 'user.selection.x1', 'selection.x1', 'selection.x1');
insert into text values (4021, NULL, 'user.selection.x2', 'selection.x2', 'selection.x2');
insert into text values (4022, NULL, 'user.selection.y2', 'selection.y2', 'selection.y2');

insert into text values (30001, NULL, 'archive', 'Archive', 'Архив');
insert into text values (30002, NULL, NULL, 'Library', 'Библиотека');
insert into text values (30003, NULL, NULL, 'Collection of natal and event charts', 'Колекция от рожденни дати и събития');
insert into text values (30004, NULL, NULL, 'Psychometrica', 'Психометрика');
insert into text values (30005, NULL, 'test_psycho_reaction', 'Reaction Test', 'Тест за реакция');
insert into text values (30006, NULL, NULL, 'Test reaction speed', 'Тества реакцията');
insert into text values (30007, NULL, 'test_psycho_color_blocks', 'Color Block Test', 'Тест с цветни блокчета');
insert into text values (30008, NULL, NULL, 'Test speed of color combination', 'Тества скорост на тырсене на цветове');
insert into text values (30009, NULL, NULL, 'Biology', 'Биология');
insert into text values (30010, NULL, NULL, 'Moon Cycle', 'Лунен цикъл');
insert into text values (30011, NULL, NULL, 'Women cycle research', 'Изследва женския цикъл');
insert into text values (30012, NULL, NULL, 'Synastry', 'Синастрия');
insert into text values (30013, NULL, NULL, 'Partner Match', 'Съвместимост между партньори');
insert into text values (30014, NULL, NULL, 'Partner Match ...', 'Съвместимост между партньори ...');
insert into text values (30016, NULL, NULL, 'Collection of personal event records', 'Записи на лични събития');
insert into text values (30017, NULL, 'relocation', 'Relocations', 'Релокация');
insert into text values (30018, NULL, NULL, 'Records of temporary and lasting personal relocations', 'Записи на промяна на местонахожденията');
insert into text values (30019, NULL, 'geography', 'Geography', 'География');
insert into text values (30020, NULL, NULL, 'Records of populated places', 'Записи на населени места');
insert into text values (30024, NULL, NULL, 'Personal Office', 'Личен кабинет');
insert into text values (30025, NULL, 'sleep', 'Sleep', 'Сън');
insert into text values (30026, NULL, NULL, 'Astronomy', 'Астрономия');
insert into text values (30027, NULL, 'sunspots', 'Sun Spots', 'Слънчеви петна');

insert into text values (40002, NULL, NULL, 'Description', 'Описание');
insert into text values (40003, NULL, NULL, 'Chart', 'Карта');
insert into text values (40004, NULL, NULL, 'Save', 'Запази');
insert into text values (40005, NULL, NULL, 'Edit Time', 'Редактирай време');
insert into text values (40006, NULL, NULL, 'Edit Location', 'Редактирай местоположение');
insert into text values (40007, NULL, NULL, 'Position of Planets', 'Позиция на планети');
insert into text values (40008, NULL, NULL, 'Position of Houses', 'Позиция на домове');
insert into text values (40009, NULL, NULL, 'Track', 'Трасе');
insert into text values (40011, NULL, NULL, 'Deselect', 'Махни селекция');
insert into text values (40016, NULL, NULL, 'Project', 'Проект');
insert into text values (40018, NULL, NULL, 'Enter data', 'Въведи данни');
insert into text values (40019, NULL, NULL, 'Result', 'Резултат');
insert into text values (40020, NULL, NULL, 'Tools', 'Инструменти');
insert into text values (40021, NULL, NULL, 'Send feedback', 'Дай мнение');
insert into text values (40022, NULL, NULL, 'Go to laboratory', 'Избери лаборатория');
insert into text values (40024, NULL, NULL, 'Details', 'Детайли');
insert into text values (40025, NULL, NULL, 'List records', 'Покажи записи');
insert into text values (40026, NULL, NULL, 'View general power', 'Покажи обща сила');
insert into text values (40027, NULL, NULL, 'View general sign match', 'Покажи обща съвместимост между знаците');
insert into text values (40028, NULL, NULL, 'Help', 'Помощ');
insert into text values (40029, NULL, NULL, 'View', 'Покажи');
insert into text values (40031, NULL, NULL, 'Send invitation', 'Прати покана');
insert into text values (40032, NULL, NULL, 'Display formula', 'Покажи формули');
insert into text values (40033, NULL, NULL, 'Edit formulae', 'Редактирай формула');
insert into text values (40034, NULL, NULL, 'Display formulae chart', 'Покажи показания на формулата');
insert into text values (40037, NULL, NULL, 'Data Table', 'Data Table');
insert into text values (40038, NULL, NULL, 'Data Chart', 'Data Chart');
insert into text values (40039, NULL, NULL, 'Data', 'Data');
insert into text values (40040, NULL, NULL, 'Natal Chart', 'Натална карта');
insert into text values (40041, NULL, NULL, 'Statistics', 'Статистически данни');

insert into text values (60001, NULL, NULL, 'Description', 'Описание');
insert into text values (60002, NULL, NULL, 'Subject', 'Субект');
insert into text values (60003, NULL, NULL, 'Location', 'Място');
insert into text values (60004, NULL, NULL, 'Accuracy', 'Точност');
insert into text values (60005, NULL, NULL, 'Source', 'Достоверност');
insert into text values (60006, NULL, NULL, 'Time of occurance', 'Време на събитие');
insert into text values (60007, NULL, NULL, 'Enter the description of the event here!', 'Въведи описанието тук!');
insert into text values (60008, NULL, NULL, 'Report any different location in project Relocation!', 'Обяви място чрез проект Релокация!');
insert into text values (60009, NULL, NULL, 'This is you.', 'Това си ти.');
insert into text values (60010, NULL, NULL, 'Hello', 'Здравей');
insert into text values (60011, NULL, NULL, 'Pro', 'За');
insert into text values (60012, NULL, NULL, 'Con', 'Против');
insert into text values (60013, NULL, NULL, 'Message', 'Съобщение');
insert into text values (60014, NULL, NULL, 'Opinion', 'Мнение');
insert into text values (60025, NULL, NULL, 'male', 'мъж');
insert into text values (60026, NULL, NULL, 'female', 'жена');
insert into text values (60027, NULL, NULL, 'event', 'събитие');
insert into text values (60028, NULL, NULL, 'a second', 'секунда');
insert into text values (60029, NULL, NULL, 'a minute', 'минута');
insert into text values (60030, NULL, NULL, '5 minutes', '5 минути');
insert into text values (60031, NULL, NULL, '10 minutes', '10 минути');
insert into text values (60032, NULL, NULL, '30 minutes', '30 минути');
insert into text values (60033, NULL, NULL, 'an hour', 'час');
insert into text values (60034, NULL, NULL, 'few hours', 'часове');
insert into text values (60035, NULL, NULL, 'a day', 'ден');
insert into text values (60036, NULL, NULL, 'a week', 'седмица');
insert into text values (60037, NULL, NULL, 'a month', 'месец');
insert into text values (60038, NULL, NULL, 'an year', 'година');
insert into text values (60039, NULL, NULL, 'accurate', 'точно');
insert into text values (60040, NULL, NULL, 'rectified', 'ректифицирано');
insert into text values (60041, NULL, NULL, 'recalled', 'припомнено');
insert into text values (60042, NULL, NULL, 'guessed', 'налучкано');
insert into text values (60043, NULL, NULL, 'planned', 'планирано');
insert into text values (60044, NULL, NULL, 'The Archive contains', 'Архивът съдържа');
insert into text values (60045, NULL, NULL, 'records of births.', 'записа на рожденни дати');
insert into text values (60046, NULL, NULL, 'Enter part of name or birthplace', 'Въведи част от име или място');
insert into text values (60047, NULL, NULL, 'Search', 'Търси');
insert into text values (60048, NULL, NULL, 'Your favourites are:', 'Често използваш:');
insert into text values (60049, NULL, NULL, 'The laboratories are:', 'Лабораториите са:');
insert into text values (60050, NULL, NULL, 'The projects are:', 'Проектите са:');
insert into text values (60051, NULL, NULL, 'Product', 'Продукт');
insert into text values (60052, NULL, NULL, 'Category', 'Категория');
insert into text values (60065, NULL, NULL, 'kilogram', 'килограм');
insert into text values (60066, NULL, NULL, 'liter', 'литър');
insert into text values (60067, NULL, NULL, 'item', 'брой');
insert into text values (60068, NULL, NULL, 'buy', 'купува');
insert into text values (60069, NULL, NULL, 'sell', 'продава');
insert into text values (60070, NULL, NULL, 'No opinions at the moment.', 'Няма мнения до момента.');
insert into text values (60071, NULL, NULL, 'Please, select a project!', 'Моля, избери проект!');
insert into text values (60072, NULL, NULL, 'Record is visible to', 'Записът е видим за');
insert into text values (60073, NULL, NULL, 'everyone', 'всеки');
insert into text values (60074, NULL, NULL, 'me only', 'мен само');
insert into text values (60075, NULL, NULL, 'From', 'От');
insert into text values (60076, NULL, NULL, 'To', 'До');
insert into text values (60077, NULL, NULL, 'sleeps', 'спи');

insert into text values (1000001, NULL, 'Europe/Sofia', 'Europe/Sofia', 'Европа/София');

insert into text values (1001001, NULL, NULL, 'Bulgaria', 'България');
insert into text values (1001002, NULL, NULL, 'Sofia', 'София');
insert into text values (1001003, NULL, NULL, 'Stara Zagora', 'Стара Загора');
insert into text values (1001004, NULL, NULL, 'Aytos', 'Айтос');
insert into text values (1001005, NULL, NULL, 'Varna', 'Варна');

insert into text values (2000001, NULL, NULL, 'Stephan Zlatarev', 'Стефан Златарев');
insert into text values (2000002, NULL, NULL, 'Svetlana Stancheva', 'Светлана Станчева');
insert into text values (2000003, NULL, NULL, 'Gufi', 'Gufi');
insert into text values (2000004, NULL, NULL, 'Altrance', 'Altrance');
insert into text values (2000005, NULL, NULL, 'EURUSD', 'EURUSD');

insert into svg values (3101, "<svg:circle r='70' style='fill:green;stroke:black;stroke-width:10' />");
insert into svg values (3102, "<svg:circle r='70' style='fill:white;stroke:black;stroke-width:10' />");

insert into svg values (2001, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M-130 0 A60 60 0 0 1 0 0 A50 50 0 0 1 130 0' /><svg:line y2='130' /></svg:g>");
insert into svg values (2002, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M-60 -110 A60 60 0 0 0 60 -110' /><svg:circle cy='30' r='80' /></svg:g>");
insert into svg values (2003, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M-60 -110 A60 30 0 0 0 60 -110' /><svg:line x1='-30' y1='-70' x2='-30' y2='70' /><svg:line x1='30' y1='-70' x2='30' y2='70' /><path d='M-60 110 A60 30 0 0 1 60 110' /></svg:g>");
insert into svg values (2004, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M90 -50 A90 30 0 0 0 -90 -50 A40 40 0 1 0 -88 -52' /><svg:path d='M-90 50 A90 30 0 0 0 90 50 A40 40 0 1 0 88 52' /></svg:g>");
insert into svg values (2005, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M-60,0 C-80,20 -30,50 -30,0 S-60,-50 -30,-100 S70,-80 70,-40 S30,50 30,60 S30,120 80,90' /></svg:g>");
insert into svg values (2006, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M-90,80 L-90,-80 S-10,-170 -30,80' /><svg:path d='M-40,-80 S50,-190 20,140' /><svg:path d='M10,-80 C90,-150 110,60 10,90' /></svg:g>");
insert into svg values (2007, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M-130 0 L-35 0 A50 50 0 1 1 35 0 L130 0' /><svg:path d='M-130 50 L130 50' /></svg:g>");
insert into svg values (2008, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M-90 80 L-90 -80 S-10 -170 -30 80' /><svg:path d='M-40 -80 S50,-170 30,80 C30,140 130,120 90,-30 L85,20' /></svg:g>");
insert into svg values (2009, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:line x1='-100' y1='100' x2='100' y2='-100' /><svg:line y1='70' x2='-70' /><svg:path d='M0 -70 L100 -100 L70 0' /></svg:g>");
insert into svg values (2010, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M-120 -50 Q-100 -50 -80 -90 Q-70 -50 0 -70 C-70 80 0 130 80 80 S0 -140 -10 140' /></svg:g>");
insert into svg values (2011, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M-120 -20 A40 40 0 0 1 -40 -20 A40 20 0 0 0 40 -20 A40 40 0 0 1 120 -20' /><svg:path d='M-120 50 A40 40 0 0 1 -40 50 A40 20 0 0 0 40 50 A40 40 0 0 1 120 50' /></svg:g>");
insert into svg values (2012, "<svg:g style='stroke:black;stroke-width:30;fill:none'><svg:path d='M-100 -100 A40 50 0 1 1 -100 100' /><svg:path d='M100 -100 A40 50 0 1 0 100 100' /><svg:line x1='-80' x2='80' /></svg:g>");
insert into svg values (2101, "<svg:g style='stroke:gold;stroke-width:20;fill:none'><svg:circle r='90' /><svg:circle r='10' /></svg:g>");
insert into svg values (2102, "<svg:path d='M-60 -80 A70 70 0 0 1 60 80 A40 70 -25 1 0 -60 -80' style='stroke:yellow;stroke-width:20;fill:none' />");
insert into svg values (2103, "<svg:g style='stroke:yellow;stroke-width:20;fill:none'><svg:circle r='45' cy='-25' /><svg:path d='M-40 -90 A80 80 0 0 0 40 -90' /><svg:line y1='30' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (2104, "<svg:g style='stroke:green;stroke-width:20;fill:none'><svg:circle r='60' cy='-30' /><svg:line y1='30' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (2105, "<svg:g style='stroke:red;stroke-width:20;fill:none'><svg:circle r='70' cx='-15' cy='15' /><svg:line x1='40' y1='-40' x2='90' y2='-90' /><svg:path d='M90 -30 L90 -90 L30 -90' /></svg:g>");
insert into svg values (2106, "<svg:g style='stroke:orange;stroke-width:20;fill:none'><svg:path d='M-90 -90 A70 70 0 0 1 -40 50 L90 50' /><svg:line x1='40' y1='10' x2='40' y2='90' /></svg:g>");
insert into svg values (2107, "<svg:g style='stroke:darkolivegreen;stroke-width:20;fill:none'><svg:path d='M-20 -90 L-20 -20 A70 70 0 0 1 60 100' /><svg:line x1='-70' y1='-60' x2='20' y2='-60' /></svg:g>");
insert into svg values (2108, "<svg:g style='stroke:cyan;stroke-width:20;fill:none'><svg:path d='M-90 -90 A50 50 0 1 1 -90 40' /><svg:path d='M90 -90 A50 50 0 1 0 90 40' /><svg:line x1='-40' y1='-30' x2='40' y2='-30' /><svg:line y1='-80' y2='30' /><svg:circle cy='65' r='25' /></svg:g>");
insert into svg values (2109, "<svg:g style='stroke:teal;stroke-width:20;fill:none'><svg:path d='M-80 -90 A80 110 0 1 0 80 -90' /><svg:line y1='-80' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (2110, "<svg:g style='stroke:firebrick;stroke-width:20;fill:none'><svg:circle cy='-60' r='30' /><svg:path d='M-80 -90 A80 110 0 1 0 80 -90' /><svg:line y1='20' y2='90' /><svg:line x1='-40' y1='60' x2='40' y2='60' /></svg:g>");
insert into svg values (2201, "<svg:path d='M-40 90 L-40 -90 L40 -90' style='stroke:brown;stroke-width:20;fill:none' />");

insert into svg values (3001, "<svg:rect x='-70' y='-70' width='140' height='140' style='fill:red' />");
insert into svg values (3002, "<svg:rect x='-70' y='-70' width='140' height='140' style='fill:orange' />");
insert into svg values (3003, "<svg:rect x='-70' y='-70' width='140' height='140' style='fill:yellow' />");
insert into svg values (3004, "<svg:rect x='-70' y='-70' width='140' height='140' style='fill:green' />");
insert into svg values (3005, "<svg:rect x='-70' y='-70' width='140' height='140' style='fill:blue' />");
insert into svg values (3006, "<svg:rect x='-70' y='-70' width='140' height='140' style='fill:indigo' />");
insert into svg values (3007, "<svg:rect x='-70' y='-70' width='140' height='140' style='fill:black' />");
insert into svg values (3008, "<svg:rect x='-70' y='-70' width='140' height='140' style='fill:white;stroke:black;stroke-width:10' />");

insert into project values (30001, 30002, 'white', now(), NULL, 30003);
insert into project values (30013, 30012, 'white', now(), NULL, 30014);
insert into project values (30017, 30024, 'white', now(), NULL, 30018);
insert into project values (30019, 30002, 'white', now(), NULL, 30020);
insert into project values (30027, 30026, 'white', now(), NULL, 0);

insert into project_geography values (0, 0, 0, 0, 0);
insert into project_geography values (1001001,0,0,0,1000001);
insert into project_geography values (1001002,1001001,-23.3167,42.67,1000001);
insert into project_geography values (1001003,1001001,-25.634,42.4167,1000001);
insert into project_geography values (1001004,1001001,-27.25,42.7,1000001);
insert into project_geography values (1001005,1001001,-27.9167,43.2167,1000001);

insert into project_archive values (2000001, 2000001, '1979-01-09 01:30:00', 1001003, 'male', '10 minutes', 'accurate');
insert into project_archive values (2000002, 2000002, '1979-04-01 00:00:00', 1001004, 'female', '10 minutes', 'accurate');
insert into project_archive values (2000003, 2000003, '1986-11-23 18:15:00', 1001002, 'female', '10 minutes', 'accurate');
insert into project_archive values (2000004, 2000004, '1984-11-23 11:34:00', 1001005, 'male', '10 minutes', 'accurate');
insert into project_archive values (2000005, 2000005, '1900-01-01 01:01:00', 1001005, 'male', '10 minutes', 'accurate');

insert into users VALUES (2000001, 'en', 'stephan.zlatarev@gmail.com', NULL);

insert into attribute values (2000001, 4001, 2000001);
insert into attribute values (2000001, 4002, 1001003);
insert into attribute values (2000001, 4010, 4012);
insert into attribute values (2000002, 4001, 2000002);
insert into attribute values (2000002, 4002, 1001004);

insert into views values (0, 'astrolab.web.component.general.PageNotImplemented');
insert into views values (1, 'astrolab.web.server.content.MenuPage');
insert into views values (2, 'astrolab.project.archive.DisplayArchiveRecordDetails');
insert into views values (4, 'astrolab.web.component.chart.Chart');
insert into views values (5, 'astrolab.project.archive.ModifyCreateNatalRecord');
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
insert into views values (23, 'astrolab.project.DisplayProjectList');
insert into views values (25, 'astrolab.project.archive.DisplayArchiveRecordList');
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
insert into views values (36, 'astrolab.project.archive.FormEditNatalRecord');
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
insert into views values (52, 'astrolab.formula.display.DisplayProjectFormula');
insert into views values (53, 'astrolab.formula.display.ModifyFormulae');
insert into views values (56, 'astrolab.web.entrance.DisplayPersonalData');
insert into views values (57, 'astrolab.project.DisplayRecordsTable');
insert into views values (58, 'astrolab.project.DisplayDataChart');
insert into views values (60, 'astrolab.formula.display.ModifyFormulaeSetChartBase');
insert into views values (61, 'astrolab.formula.display.ModifyFormulaeSetChartColor');
insert into views values (62, 'astrolab.formula.display.ModifyFormulaeSetTime');

insert into views_perspective values (0, '<frameset cols="15%,*" border="0"><frame src="/view.html?_d=1" /><frame src="/view.html?_d=56" /></frameset>');
insert into views_perspective values (40040, '<frameset cols="15%,*" border="0"><frameset rows="20%,40%,40%"><frame src="/view.html?_d=1" /><frame src="/view.html?_d=2" /><frame src="/view.html?_d=25" /></frameset><frameset rows="70%,*"><frameset cols="70%,15%,15%"><frame src="/view.html?_d=4" /><frame src="/view.html?_d=10" /><frame src="/view.html?_d=11" /></frameset><frame src="/view.html?_d=45" /></frameset></frameset>');
insert into views_perspective values (40041, '<frameset cols="15%,*" border="0"><frameset rows="30%,*"><frame src="/view.html?_d=1" /><frame src="/view.html?_d=23" /></frameset><frameset rows="70%,*"><frameset cols="70%,30%"><frame src="/view.html?_d=58" /><frame src="/view.html?_d=52" /></frameset><frame src="/view.html?_d=45" /></frameset></frameset>');
insert into views_perspective values (40018, '<frameset cols="15%,*" border="0"><frameset rows="30%,*"><frame src="/view.html?_d=1" /><frame src="/view.html?_d=23" /></frameset><frameset cols="50%,*"><frame src="/view.html?_a=40018" /><frame src="/view.html?_a=40019" /></frameset></frameset>');

insert into actions values (40002, 40016, NULL, NULL, NULL, NULL, 45, NULL);
insert into actions values (40003, 40020, 4016, NULL, NULL, NULL, 4, NULL);
insert into actions values (40004, NULL, NULL, NULL, 36, 5, 2, NULL);
insert into actions values (40004, NULL, NULL, NULL, 6, 7, 2, NULL);
insert into actions values (40004, NULL, 4016, NULL, 8, 9, 2, NULL);
insert into actions values (40004, NULL, NULL, NULL, 28, 29, 2, NULL);
insert into actions values (40004, NULL, NULL, NULL, 31, 32, 30, NULL);
insert into actions values (40004, NULL, NULL, NULL, 37, 38, 33, NULL);
insert into actions values (40004, NULL, NULL, NULL, 40, 41, 39, NULL);
insert into actions values (40004, NULL, NULL, NULL, 43, 44, 42, NULL);
insert into actions values (40004, NULL, NULL, NULL, 48, 49, 50, NULL);
insert into actions values (40004, NULL, NULL, NULL, 52, 53, 52, NULL);
insert into actions values (40005, NULL, NULL, NULL, 2, NULL, 6, NULL);
insert into actions values (40006, NULL, NULL, NULL, 2, NULL, 8, NULL);
insert into actions values (40007, 40029, 4016, NULL, NULL, NULL, 10, NULL);
insert into actions values (40008, 40029, 4016, NULL, NULL, NULL, 11, NULL);
insert into actions values (40009, NULL, 4016, NULL, NULL, NULL, 12, NULL);
insert into actions values (40011, NULL, 4016, NULL, 25, 17, 25, NULL);
insert into actions values (40011, NULL, 4017, NULL, 2, 18, 2, NULL);
insert into actions values (40011, NULL, 4016, NULL, 30, 17, 30, NULL);
insert into actions values (40018, 40016, NULL, 30001, NULL, NULL, 36, NULL);
insert into actions values (40018, 40016, NULL, 30013, NULL, NULL, 25, NULL);
insert into actions values (40018, 40016, NULL, 30017, NULL, NULL, 31, NULL);
insert into actions values (40018, 40016, NULL, 30019, NULL, NULL, 37, NULL);
insert into actions values (40018, 40016, NULL, 30025, NULL, NULL, 48, NULL);
insert into actions values (40019, 40016, NULL, 30001, NULL, NULL, 25, NULL);
insert into actions values (40019, 40016, NULL, 30013, NULL, NULL, 34, NULL);
insert into actions values (40019, 40016, NULL, 30017, NULL, NULL, 30, NULL);
insert into actions values (40019, 40016, NULL, 30019, NULL, NULL, 33, NULL);
insert into actions values (40019, 40016, NULL, 30027, NULL, NULL, 57, NULL);
insert into actions values (40021, 40028, NULL, NULL, NULL, NULL, 35, NULL);
insert into actions values (40024, 40029, NULL, NULL, NULL, NULL, 2, NULL);
insert into actions values (40025, 40016, NULL, 30001, NULL, NULL, 25, NULL);
insert into actions values (40025, 40016, NULL, 30017, NULL, NULL, 30, NULL);
insert into actions values (40025, 40016, NULL, 30019, NULL, NULL, 33, NULL);
insert into actions values (40026, 40016, NULL, 30013, NULL, NULL, 26, NULL);
insert into actions values (40027, 40016, NULL, 30013, NULL, NULL, 27, NULL);
insert into actions values (40031, 40028, NULL, NULL, NULL, NULL, 46, NULL);
insert into actions values (40031, 40028, NULL, NULL, 46, 47, 46, NULL);
insert into actions values (40033, 40039, NULL, NULL, NULL, NULL, 52, NULL);
insert into actions values (40037, 40039, NULL, 30027, NULL, NULL, 57, NULL);
insert into actions values (40038, 40039, NULL, 30027, NULL, NULL, 58, NULL);

