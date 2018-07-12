/**
 * 菜单管理
 * 
 */

angular.module('inspinia',['uiSwitch']).controller('menuCtrl',
		function($scope, $http, $state, $stateParams, i18nService, SweetAlert,$httpParamSerializerJQLike) {
			$scope.menuType = [{text:"菜单",value:"menu"},{text:"页面操作",value:"page"}];
			$scope.levelType = [{text:"0",value:0},{text:"1",value:1},{text:"2",value:2}];
			$scope.menuTypeAll = [{text:"全部",value:""},{text:"菜单",value:"menu"},{text:"页面操作",value:"page"}];
			i18nService.setCurrentLang('zh-cn');
			var flags = [false, false];
			$scope.treeConfig = {
				'plugins' : [ 'types' ],
				types : {
					'default' : { icon : 'fa fa-default' },
					folder: { icon : 'fa fa-folder' },
					leaf : { icon : 'fa fa-leaf' }
				},
				version: 1
			};
						
			$scope.reCreateTree = function(reparse) {
				$scope.treeConfig.version++;
			}

			var openedIds = {};
			
			$scope.openNodeCB = function(ev, data){
				openedIds[data.node.id]=1;
			}
			$scope.closeNodeCB = function(ev, data){
				delete openedIds[data.node.id];
			}
			
			$scope.changedCB = function(ev, data) {
				if (data.action != "select_node")
					return;
				$scope.$apply(function() {
					var item = data.node.original;
					$scope.currMenu = item;
					$scope.refreshPageList();
				});
			}

			$scope.reCreateTree = function(reparse) {
				$scope.treeConfig.version++;
			};
			$scope.refreshTree = function(updateFlag){
				flags[1] = false;
				$http.get("menu/selectMenuAndChildren?id=0").success(function(data) {
					$scope.allPages = data.allPages;
					var parents = [];
					function formatSysMenuData(data) {
						$scope.allPages.unshift(data);
						data.text = data.menuName;
						if (data.children && data.children.length) {
							parents.push(data.id);
							angular.forEach(data.children, formatSysMenuData);
							parents.pop();
							data.type = "folder";
							data.state = { opened : true };
						} else {
							data.type = "leaf";
							delete data.children;
						}
						if(updateFlag&&$scope.currMenu.id==data.id){
							$scope.currMenu = data;
							if(!$scope.currMenu.state)
								$scope.currMenu.state = {};
							$scope.currMenu.state.selected=true;
						}
						return data;
					}
					angular.forEach(data.allMenus, formatSysMenuData);
					
					$scope.sysMenus = data.allMenus;
					if(!updateFlag){
						$scope.currMenu = data.allMenus[0];
						if(!$scope.currMenu.state)
							$scope.currMenu.state = {};
						$scope.currMenu.state.selected=true;
					}
					flags[1] = true;
					$scope.refreshPermits();
					$scope.reCreateTree();
				});
			};
			
			$scope.refreshPermits = function(){
				$scope.showPages = [];
				angular.forEach($scope.allPages,function(data,index){
					if((data.menuType=='page' && data.parentId==$scope.currMenu.id) || data.id == $scope.currMenu.id){
						$scope.showPages.push(data);
					}
				});
			};
			
			//增加新的菜单
			$scope.menuModal = function(){
				$scope.newMenuInfo = {parentId:$scope.currMenu.id,menuLevel:1,menuType:"menu"};
				$scope.type=1;
				$("#menuModal").modal("show");
			}
            //增加新的菜单
            $scope.pageModal = function(){
                $scope.newMenuInfo = {parentId:$scope.currMenu.id,menuLevel:2,menuType:"page"};
                $scope.type=1;
                $("#menuModal").modal("show");
            }
			//修改菜单
			$scope.editMenuModal = function(entity){
				$scope.newMenuInfo = angular.copy(entity);
//				if($scope.newMenuInfo.orderNo!=null){
//					$scope.newMenuInfo.orderNo = $scope.newMenuInfo.orderNo.substr($scope.newMenuInfo.orderNo.length-2,2);
//				}
				$scope.type=2;
				$("#menuModal").modal("show");
			}
			$scope.cancel=function(){
				$("#menuModal").modal("hide");
			}
			$scope.deleteMenu=function(id){
		        SweetAlert.swal({
		            title: "确认删除？",
//		            text: "",
		            type: "warning",
		            showCancelButton: true,
		            confirmButtonColor: "#DD6B55",
		            confirmButtonText: "提交",
		            cancelButtonText: "取消",
		            closeOnConfirm: true,
		            closeOnCancel: true },
			        function (isConfirm) {
			            if (isConfirm) {
			            	$http.post("menu/deleteMenu.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			        		.success(function(msg){
			        			$scope.notice(msg.msg);
			        			$scope.query();
			        		}).error(function(){
			        		});
			            }
		        });
		    };
			
			$scope.refreshTree(false);
			$scope.refreshPageList = function(){
				$scope.gridOptions.data = [];
				$http({  
					   method:'post',  
					   url:"menu/findMenuPageList",
					   data:$httpParamSerializerJQLike({menuId: $scope.currMenu.id}),  
					   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}
					})
				.success(function(data){
					if(data && data.length){
						$scope.gridOptions.data= data;
//						angular.forEach($scope.gridOptions.data,function(data2){
//							data2.logFlag=1;
//						})
					}
					$scope.gridOptions.data.unshift($scope.currMenu);
				});
				
			}
			
			$scope.gridOptions={                           //配置表格
//					data:"pages",
					enableSorting:true,
				      columnDefs:[                           //表格数据
//				         { field: 'id',displayName:'序号'},
				         { field: 'menuName',displayName:'名称'},
				         { field: 'menuCode',displayName:'功能编号' },
				         { field: 'menuUrl',displayName:'功能路径' },
//				         { field: 'logFlag',displayName:'是否记录日志',cellTemplate: 
//				        	 '<switch class="switch switch-s" ng-model="row.entity.logFlag" ng-change="grid.appScope.switchStatus(row)" />'
//				            },
				         { field: 'orderNo',displayName:'排序号'},
				         { field: 'id',displayName:'操作', width: 80, enableSorting: false,
				        	 cellTemplate: '<div class="ui-grid-cell-contents"><a ng-show="grid.appScope.hasPermit(\'menu.update\')" ng-click="grid.appScope.editMenuModal(row.entity)">修改</a>'
				        		  + '<a ng-show="grid.appScope.hasPermit(\'menu.delete\')"  ng-click="grid.appScope.deleteMenu(row.entity.id)"> | 删除</a></div>'
				         }
				      ],
				};
			
			//修改状态
			$scope.switchStatus=function(row){
				if(row.entity.logFlag){
					$scope.serviceText = "确定开启？";
				} else {
					$scope.serviceText = "确定关闭？";
				}
		        SweetAlert.swal({
		            title: $scope.serviceText,
//		            text: "服务状态为关闭后，不能正常交易!",
		            type: "warning",
		            showCancelButton: true,
		            confirmButtonColor: "#DD6B55",
		            confirmButtonText: "提交",
		            cancelButtonText: "取消",
		            closeOnConfirm: true,
		            closeOnCancel: true },
			        function (isConfirm) {
			            if (isConfirm) {
			            	if(row.entity.logFlag==true){
			            		row.entity.logFlag=1;
			            	} else if(row.entity.logFlag==false){
			            		row.entity.logFlag=0;
			            	}
			            	var data={"logFlag":row.entity.logFlag,"id":row.entity.id};
			                $http.post("menu/switchLogFlag.do",angular.toJson(data))
			            	.success(function(data){
			            		if(data.status){
			            			$scope.notice("操作成功");
			            		}else{
			            			if(row.entity.logFlag==true){
			    	            		row.entity.logFlag = false;
			    	            	} else {
			    	            		row.entity.logFlag = true;
			    	            	}
			            			$scope.notice("操作失败");
			            		}
			            	})
			            	.error(function(data){
			            		if(row.entity.logFlag==true){
		    	            		row.entity.logFlag = false;
		    	            	} else {
		    	            		row.entity.logFlag = true;
		    	            	}
			            		$scope.notice("服务器异常")
			            	});
			            } else {
			            	if(row.entity.logFlag==true){
			            		row.entity.logFlag = false;
			            	} else {
			            		row.entity.logFlag = true;
			            	}
			            }
		        });
		    	
		    };
			
			$scope.submitNewMenu = function(){
				var url="";
				if($scope.type==1){
					url="menu/saveMenu.do";
				}else{
					url="menu/updateMenu.do";
				}
				$scope.submitting = true;
				$http.post(url,$scope.newMenuInfo)
				.success(function(msg){
					if(msg.status){
						$("#menuModal").modal("hide");
						$scope.refreshPageList();
						$scope.refreshTree(true);
					}
					$scope.notice(msg.msg);
					$scope.submitting = false;
				}).error(function(){
					$scope.notice("服务异常");
					$scope.submitting = false;
				});
			}
			
			$scope.deleteMenu=function(id){
		        SweetAlert.swal({
		            title: "确认删除？",
//		            text: "",
		            type: "warning",
		            showCancelButton: true,
		            confirmButtonColor: "#DD6B55",
		            confirmButtonText: "提交",
		            cancelButtonText: "取消",
		            closeOnConfirm: true,
		            closeOnCancel: true },
			        function (isConfirm) {
			            if (isConfirm) {
			            	$http.post("menu/deleteMenu.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			        		.success(function(msg){
			        			$scope.notice(msg.msg);
			        			$scope.refreshPageList();
			        			$scope.refreshTree(true);
			        		}).error(function(){
			        		});
			            }
		        });
		    };

			$scope.changeLevel = function(){
				if($scope.newMenuInfo.menuLevel == null){
					return;
				}
				if($scope.newMenuInfo.menuLevel==2){
                    $scope.newMenuInfo.menuType = "page";
				}else{
                    $scope.newMenuInfo.menuType = "menu";
				}
			}
		});