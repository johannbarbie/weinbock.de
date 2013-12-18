define(['backbone', 'communicator', 'hbs!tmpl/item/loginView_tmpl'], function(Backbone, Communicator, LoginTmpl) {
    'use strict';
    return Backbone.Marionette.ItemView.extend({
        template: LoginTmpl,
        className: 'container',
        initialize: function(opt) {
            this.next = opt.next;
			this.model.on('change:roles', this.onRolesChange, this);
            this.model.on('error', this.onError, this);
        },
        events: {
            'click #loginBtn':'handleLogin',
            'click .close': 'handleClose',
            'change input': 'changeInput'
        },
        changeInput: function() {
            this.$('#loginBtn').button('reset');
        },
        handleLogin: function(e) {
            e.preventDefault();
            $(e.target).button('loading');
            var user = this.$('input:text').val();
            var pw = this.$('input:password').val();
            if (user && pw){
                var cred = {
                    username: user,
                    password: pw
                };
                this.model.clear({silent:true});
                this.model.set({
                    locale: window.opt.lng,
                    basePath: window.opt.basePath,
                    srvcPath: window.opt.srvcPath
                });
                this.model.credentials = cred;
                this.model.fetch();
            }
        },
        onRolesChange: function(){
            this.next();
        },
        onError: function(){
            this.$('.alert').css('display','');
            this.$('.alert').addClass('in');
            this.$('#loginBtn').button('reset');
        },
        handleClose: function(e){
            var alert = $(e.target).parent();
            alert.one(window.transEvent(), function(){
                alert.css('display', 'none');
            });
            alert.removeClass('in');
        },
        onShow:function () {
            this.$('.alert').css('display', 'none');
            this.$('#loginBtn').prop('disabled',true);
        }
    });
});