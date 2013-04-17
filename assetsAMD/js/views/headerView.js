define(['underscoreM', 'marionette', 'templates', 'bootstrap'], function(_, Marionette, templates) {
    'use strict';
    return Marionette.ItemView.extend({
        template: _.template(templates.header),
        className: "header"
    });
});