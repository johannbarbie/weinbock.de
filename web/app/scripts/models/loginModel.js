define(['backbone', 'communicator'], function(Backbone, Communicator) {
    'use strict';

    // private
    var LoginStatus = Backbone.Model.extend({
        url: window.opt.basePath+'/account/profile',
        defaults: {
            id: sessionStorage.getItem('id'),
            roles: (sessionStorage.getItem('roles'))?[sessionStorage.getItem('roles')]:undefined,
            email: (sessionStorage.getItem('email'))?sessionStorage.getItem('email'):undefined,
        },

        initialize: function(){
            this.credentials = sessionStorage.getItem('credentials');
            this.on('change:roles', function (model){
                sessionStorage.setItem('credentials',JSON.stringify(model.credentials));
            });
            var vent = Communicator.mediator;
            var self = this;
            vent.on('app:logout', function(){
                sessionStorage.clear();
                self.credentials = null;
                self.clear();
            });
        },

        parse: function(response) {
            if (response){
                if (response.id){
                    response.cn = new RegExp('cn=([^,]+),').exec(response.id)[1];
                }
                sessionStorage.setItem('id',response.id);
                sessionStorage.setItem('roles',response.roles);
                sessionStorage.setItem('email',response.cn);
            }
            return response;
        }
    });
    return LoginStatus;

});