create table help_project (
  comment_id INT UNSIGNED NOT NULL REFERENCES text (id),
  commenter_id INT UNSIGNED NOT NULL REFERENCES users (user_id),
  project_id INT UNSIGNED NOT NULL REFERENCES text (id),

  INDEX (comment_id)
) ENGINE=InnoDB;

update views set request = 'astrolab.web.component.help.DisplayProjectDescription' WHERE view = 45;