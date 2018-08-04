
function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider, KeepaliveProvider, $httpProvider) {
    // Configure Idle settings
    IdleProvider.idle(5); // in seconds
    IdleProvider.timeout(120); // in seconds

    $urlRouterProvider.otherwise("/welcome/index");
    var csrfHeadName = $("meta[name=_csrf_header]").attr('content');
    var csrfData = $("meta[name=_csrf]").attr('content');
    $httpProvider.defaults.headers.common[csrfHeadName]=csrfData;  
    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    
    $httpProvider.interceptors.push(function($q,$rootScope){
        // toastr.options = {
        //     "closeButton": false,
        //     "debug": false,
        //     "newestOnTop": false,
        //     "progressBar": true,
        //     "positionClass": "toast-top-right",
        //     "preventDuplicates": false,
        //     "onclick": null,
        //     "showDuration": "300",
        //     "hideDuration": "1000",
        //     "timeOut": "8000",
        //     "extendedTimeOut": "1000",
        //     "showEasing": "swing",
        //     "hideEasing": "linear",
        //     "showMethod": "fadeIn",
        //     "hideMethod": "fadeOut"
        // };
    	return {
			responseError : function(rejection) {
				if (rejection.status === 401) {
					location.reload();
				}  else if(rejection.status === 403){
                    // toastr["error"]("没权限");
//                    $rootScope.back();
					alert('没有足够的权限')
					history.go(-1)
//					SweetAlert.swal({
//			             title: "是否重新登录？",
//			             text: "登录状态已过期，可以选择否，另外打开窗口再登录，回来继续操作",
//			             type: "warning",
//			             showCancelButton: true,
//			             confirmButtonColor: "#DD6B55",
//			             confirmButtonText: "确定",
//			             cancelButtonText: "取消",
//			             closeOnConfirm: true,
//			             closeOnCancel: true },
//				         function (isConfirm) {
//				             if (isConfirm) {
//				             	location.reload();
//				             }
//			         });
				} else if(rejection.status === 449){
					location.reload();
				}
				return $q.reject(rejection);
			}
		};
    });
    
    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false,
        modules: [
            {
                name: 'ngGrid',
                files: ['js/plugins/nggrid/ng-grid.css', 'js/plugins/nggrid/ng-grid-2.0.3.min.js'],
                cache: true
            }, {
                name: 'datePicker',
                files: ['css/plugins/datapicker/angular-datapicker.css',
                    'js/plugins/datapicker/angular-datepicker.js'],
                cache: true
            }, {
                name: 'ui.select',
                files: ['js/plugins/ui-select/select.min.js', 'css/plugins/ui-select/select.min.css'],
                cache: true
            }, {
                name: 'colorpicker.module',
                files: ['css/plugins/clockpicker/clockpicker.css', 'js/plugins/clockpicker/clockpicker.js',
                    'css/plugins/colorpicker/colorpicker.css', 'js/plugins/colorpicker/bootstrap-colorpicker-module.js'],
                cache: true
            }, {
                name: 'infinity-chosen',
                files: ['css/plugins/chosen/chosen.css', 'js/plugins/chosen/chosen.jquery.js', 'js/plugins/chosen/infinity-angular-chosen.js'],
                cache: true
            }, {
                name: 'localytics.directives',
                files: ['css/plugins/chosen/chosen.css', 'js/plugins/chosen/chosen.jquery.js', 'js/plugins/chosen/chosen.js'],
                cache: true
            }, {
                name: 'oitozero.ngSweetAlert',
                files: ['js/plugins/sweetalert/sweetalert.min.js', 'css/plugins/sweetalert/sweetalert.css'
                    , 'js/plugins/sweetalert/angular-sweetalert.min.js']
            },
            {
            	 name: 'fileUpload',
                 files: ['js/angular/angular-file-upload.min.js']
            },{
                name: 'angular-summernote',
                files: ['js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css']
            },{
            	name: 'summernote',
                files: ['js/plugins/summernote/summernote.min.js']
            },{
            	name: 'ui-switch',
                files: ['css/plugins/angular-ui-switch/angular-ui-switch.css', 'js/plugins/angular-ui-switch/angular-ui-switch.js']
            },{
            	name: 'ngJsTree',
            	files: ['js/plugins/jsTree/jstree.min.js', 'js/plugins/jsTree/ngJsTree.min.js', 'css/plugins/jsTree/style.min.css'],
                cache: true
            },{
                name: 'fancybox',
                files: ['css/plugins/fancybox/jquery.fancybox.css', 'js/plugins/fancybox/jquery.fancybox.pack.js']
            }, {//新的时间控件
                name: 'My97DatePicker',
                files: ['js/plugins/My97DatePicker/WdatePicker.js']
            }
        ]
    });
    $stateProvider
    	/*欢迎页面*/
	    .state('welcome', {
	        abstract: true,
	        url: "/welcome",
	        templateUrl: "views/common/content.html",
	    })
	    .state('welcome.index', {
	        url: "/index",
	        data: {pageTitle: '欢迎登陆'},
	        templateUrl: "views/welcome.html",
	    })
	    //======================================================================== 
        /*用户中心*/
        .state('user', {
            abstract: true,
            url: "/user",
            templateUrl: "views/common/content.html",
        })
        .state('user.user', {
            url: "/user/user",
            views:{
                "":{templateUrl:"views/user/user.html"},
                "modalRole@user.user":{templateUrl:"views/user/modalRole.html"},
                "modalRight@user.user":{templateUrl:"views/user/modalRight.html"}
            },
            data:{pageTitle: '用户管理'},
            controller: 'userCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/userCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('user.userAddMenu', {
            url: "/user/userAddMenu:id/:userName",
            templateUrl: "views/user/userAddMenu.html",
            data:{pageTitle: '权限添加菜单'},
            controller: 'userAddMenuCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/userAddMenuCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('user.role', {
            url: "/user/role",
            views:{
                "":{templateUrl:"views/user/role.html"},
                "modalUser@user.role":{templateUrl:"views/user/modalUser.html"},
                "modalRight@user.role":{templateUrl:"views/user/modalRight.html"}
            },
            data: {pageTitle: "角色管理"},
            controller: "roleCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngJsTree');
                },
                deps: ["$ocLazyLoad",function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name:"inspinia",
                        files: ["js/controllers/user/roleCtrl2.js"]
                    });
                }]
            }
        })
        .state('user.right', {
            url: "/user/right",
            views:{
                "":{templateUrl:"views/user/right.html"},
                "modalUser@user.right":{templateUrl:"views/user/modalUser.html"},
                "modalRole@user.right":{templateUrl:"views/user/modalRole.html"},
                "modalMenu@user.right":{templateUrl:"views/user/modalMenu.html"}
            },
            data: {pageTitle: "权限管理"},
            controller: "rightCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ["$ocLazyLoad",function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: "inspinia",
                        files: ["js/controllers/user/rightCtrl.js?ver="+verNo]
                    });
                }]
            }
        })
        .state('user.rightAddMenu', {
            url: "/user/rightAddMenu:id/:rightName",
            templateUrl: "views/user/role2.html",
            data:{pageTitle: '权限添加菜单'},
            controller: 'roleCtrl2',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngJsTree');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/roleCtrl2.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('user.menu', {
            url: "/user/menu",
            views:{
                "":{templateUrl: "views/user/menu2.html"},
                "modalRight@user.menu":{templateUrl:"views/user/modalRight.html"}
            },
            data:{pageTitle: '菜单管理'},
            controller: 'menuCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngJsTree');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/menuCtrl2.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('user.menuAddRight', {
            url: "/user/menuAddRight:id",
            templateUrl: "views/user/menuAddRight.html",
            data:{pageTitle: '菜单添加权限'},
            controller: 'menuAddRightCtrl',
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/menuAddRightCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('user.dictionary', {
            url: "/user/dictionary",
            templateUrl: "views/user/dictionary.html",
            data: {pageTitle: "数据字典"},
            controller: "dictionaryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ["$ocLazyLoad",function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: "inspinia",
                        files: ["js/controllers/user/dictionaryCtrl.js?ver="+verNo]
                    });
                }]
            }
        })
        .state('user.addUser', {
            url: "/addUser",
            templateUrl: "views/user/addUser.html",
            data: {pageTitle: '新增用户'},
            controller: "addUserCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/addUserCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('user.sensitive', {
            url: "/sensitive",
            templateUrl: "views/user/sensitiveWords/querySensitiveWords.html",
            data: {pageTitle: '敏感词查询'},
            controller: "querySensitiveWordsCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/sensitiveWords/querySensitiveWordsCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('user.batchSensitive', {
            url: "/batchSensitive",
            templateUrl: "views/user/sensitiveWords/leadInSensitiveWords.html",
            data: {pageTitle: '敏感词导入'},
            controller: "leadInSensitiveWordsCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/sensitiveWords/leadInSensitiveWordsCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         .state('user.job', {
            url: "/user/job",
            templateUrl: "views/user/job.html",
            data: {pageTitle: '现货任务管理'},
            controller: "jobCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ["$ocLazyLoad",function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: "inspinia",
                        files: ["js/controllers/user/jobCtrl.js?ver="+verNo]
                    });
                }]
            }
        })
        .state('user.futureJob', {
        	url: "/user/futureJob",
        	templateUrl: "views/user/futureJob.html",
        	data: {pageTitle: '期货任务管理'},
        	controller: "futureJobCtrl",
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('ui-switch');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        		},
        		deps: ["$ocLazyLoad",function($ocLazyLoad){
        			return $ocLazyLoad.load({
        				name: "inspinia",
        				files: ["js/controllers/user/futureJobCtrl.js?ver="+verNo]
        			});
        		}]
        	}
        })
        .state('user.risk', {
        	url: "/user/risk",
        	templateUrl: "views/user/risk.html",
        	data: {pageTitle: '风控配置管理'},
        	controller: "riskCtrl",
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('ui-switch');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        		},
        		deps: ["$ocLazyLoad",function($ocLazyLoad){
        			return $ocLazyLoad.load({
        				name: "inspinia",
        				files: ["js/controllers/user/riskCtrl.js?ver="+verNo]
        			});
        		}]
        	}
        })
        .state('user.hedging', {
        	url: "/user/hedging",
        	templateUrl: "views/user/hedging.html",
        	data: {pageTitle: '对冲配置管理'},
        	controller: "hedgingCtrl",
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('ui-switch');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        		},
        		deps: ["$ocLazyLoad",function($ocLazyLoad){
        			return $ocLazyLoad.load({
        				name: "inspinia",
        				files: ["js/controllers/user/hedgingCtrl.js?ver="+verNo]
        			});
        		}]
        	}
        })
        .state('user.order', {
        	url: "/user/order",
        	templateUrl: "views/user/order.html",
        	data: {pageTitle: '摆单配置管理'},
        	controller: "orderCtrl",
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('ui-switch');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        		},
        		deps: ["$ocLazyLoad",function($ocLazyLoad){
        			return $ocLazyLoad.load({
        				name: "inspinia",
        				files: ["js/controllers/user/orderCtrl.js?ver="+verNo]
        			});
        		}]
        	}
        })
}


angular.module('inspinia')
    .config(config)
    .run(function ($rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
        $rootScope.$on("$stateChangeSuccess",  function(event, toState, toParams, fromState, fromParams) {
            // to be used for back button //won't work when page is reloaded.
            $rootScope.previousState_name = fromState.name;
            $rootScope.previousState_params = fromParams;
        });
        //back button function called from back button's ng-click="back()"
        $rootScope.back = function() {//实现返回的函数
            $state.go($rootScope.previousState_name,$rootScope.previousState_params);
        };
    });
