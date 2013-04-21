require.config({
    paths: {
        jquery: '../components/jquery/jquery',
        underscore: '../components/underscore-amd/underscore', // amd version
        underscoreM: 'libs/underscore/underscore-mustache',  // templating supporting mustache style {{ ... }}
        backbone: '../components/backbone-amd/backbone', // amd version
        'backbone.wreqr': '../components/backbone.wreqr/lib/amd/backbone.wreqr', // amd version
        'backbone.eventbinder': '../components/backbone.eventbinder/lib/amd/backbone.eventbinder', // amd version
        'backbone.babysitter': '../components/backbone.babysitter/lib/amd/backbone.babysitter', // amd version
        marionette: '../components/marionette/lib/core/amd/backbone.marionette',  // amd version
        bootstrap: '../components/sass-bootstrap/docs/assets/js/bootstrap',
        text: '../components/requirejs-text/text'
    },
    shim: {
        bootstrap: {
            deps: ['jquery'],
            exports: 'jquery'
        }
    }
});

require(['app', 'controllers/librarycontroller', 'controllers/libraryrouter', 'controllers/pageApp'], function(App, LibraryController, LibraryRouter, PageApp) {
    'use strict';

    var options = {
            libraryController: LibraryController,
            libraryRouter: LibraryRouter,
            pageApp: PageApp
        };

    App.start(options);
});
