define(['marionette', 'views/templateView', 'views/faqView', 'vent', 'templates'], function(Marionette, TemplateView, FaqView, vent, templates) {
    'use strict';

    var Controller = {};

    // private module/app router  capture route and call start method of our controller
    Controller.Router = Marionette.AppRouter.extend({
        appRoutes: {
            '': 'showIndex',
            'page/faq': 'showFaq',
            'page/:tmplt': 'showTemplate'
        }
    });

    Controller.showIndex = function() {
        this.showTemplate('index');
    };

    Controller.showFaq = function() {
        var view = new FaqView({loc:null});
        vent.trigger('app:show', view);
    };

    Controller.showTemplate = function(tmplt) {
        if (!templates[tmplt]){
            tmplt = 'index';
        }
        var view = new TemplateView({tmplt:templates[tmplt]});
        vent.trigger('app:show', view);
    };

    return Controller;
});