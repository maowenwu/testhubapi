/**
 * 用户中心-角色管理 
*/
angular.module('inspinia').controller('rightCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = {};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.rightGrid = {
		data: 'rightData',
		enableSorting: true,
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,
		columnDefs: [
//            {field: 'id', displayName: '角色ID',width:150},
            {field: 'rightName', displayName: '角色名称'},
            {field: 'rightCode',displayName: '角色编码'},
//            {field: 'rightType', displayName: '角色类型',width:150},
//            {field: 'rightComment', displayName: '角色内容',width:400},
//          {field: 'status', displayName: '状态', cellFilter:"formatDropping:"+ angular.toJson($scope.status)},
            {field: 'options', displayName: '操作', cellTemplate:
            	 '<a  class="lh30"  ng-show="grid.appScope.hasPermit(\'right.update\')" ng-click="grid.appScope.editRightModal(row.entity)">修改</a>'
                +'<a class="lh30"  ng-show="grid.appScope.hasPermit(\'right.menu\')" ui-sref="user.rightAddMenu({id:row.entity.id,rightName:row.entity.rightName})"> | 菜单</a>'
                +'<a class="lh30"  ng-show="grid.appScope.hasPermit(\'right.delete\')&&row.entity.rightCode!=\'ADMIN\'" ng-click="grid.appScope.deleteRight(row.entity.id)"> | 删除</a>'
            }
        ],
        onRegisterApi: function(gridApi){
        	$scope.gridApi = gridApi;
        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
        		$scope.paginationOptions.pageNo = newPage;
        		$scope.paginationOptions.pageSize = pageSize;
        		$scope.query();
        	});
        }
	};
	//用户表
	$scope.userGrid = {
			data: 'userGroup',
			useExternalPagination: true,
			enableHorizontalScrollbar: 0,
			enableVerticalScrollbar: 1,
			columnDefs: [
	            {field: 'id',width:207,pinnable: false,sortable: false, displayName: 'ID'},
	            {field: 'userName', width:207,pinnable: false,sortable: false, displayName: '用户名称'},
	            {field: 'realName', width:207,pinnable: false,sortable: false, displayName: '真实姓名'},
	            {field: 'status', width:207,pinnable: false,sortable: false, displayName: '用户状态'}
	        ],
	        onRegisterApi: function(gridApi){
	        	$scope.userGridApi = gridApi;
	        },
	        isRowSelectable: function(row){ // 选中行 
				if($scope.rightUsers != null){
					for(var i=0;i<$scope.rightUsers.length;i++){
						 if(row.entity.id==$scope.rightUsers[i].id){
							 row.grid.api.selection.selectRow(row.entity);
						 }
					}
				}
	        },
		};
	$scope.roleGrid = {
			data: 'roleGroup',
			useExternalPagination: true,
			enableHorizontalScrollbar: 0,
			enableVerticalScrollbar: 1,
			columnDefs: [
	            {field: 'id',width:207,pinnable: false,sortable: false, displayName: 'ID'},
	            {field: 'roleName', width:207,pinnable: false,sortable: false, displayName: '角色名称'},
	            {field: 'roleCode', width:207,pinnable: false,sortable: false, displayName: '角色编码'},
	            {field: 'roleRemark', width:207,pinnable: false,sortable: false, displayName: '备注'}
	        ],
	        onRegisterApi: function(gridApi){
	        	$scope.roleGridApi = gridApi;
	        },
	        isRowSelectable: function(row){ // 选中行 
				if($scope.rightRoles != null){
					for(var i=0;i<$scope.rightRoles.length;i++){
						 if(row.entity.id==$scope.rightRoles[i].id){
							 row.grid.api.selection.selectRow(row.entity);
						 }
					}
				}
	        },
		};
	$scope.menuGrid = {
			data: 'menuGroup',
			useExternalPagination: true,
			enableHorizontalScrollbar: 0,
			enableVerticalScrollbar: 1,
			columnDefs: [
	            {field: 'id',width:207,pinnable: false,sortable: false, displayName: 'ID'},
	            {field: 'menuName', width:207,pinnable: false,sortable: false, displayName: '菜单名称'},
	            {field: 'menuCode',  width:207,pinnable: false,sortable: false, displayName: '菜单编码'},
	            {field: 'menuType', width:207,pinnable: false,sortable: false, displayName: '类型'}
	        ],
	        onRegisterApi: function(gridApi){
	        	$scope.menuGridApi = gridApi;
	        },
	        isRowSelectable: function(row){ // 选中行 
				if($scope.rightMenus != null){
					for(var i=0;i<$scope.rightMenus.length;i++){
						 if(row.entity.id==$scope.rightMenus[i].id){
							 row.grid.api.selection.selectRow(row.entity);
						 }
					}
				}
	        },
		};
	
	//查询
	$scope.query = function(){
		$http.post('right/selectRightByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.rightData = page.result;
					$scope.rightGrid.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.query();
	
	//增加新的角色
	$scope.addRightModal = function(){
		$scope.newRightInfo = {};
		$("#addRightModal").modal("show");
	}
	//修改角色
	//备份角色编码，如果修改时发生改变，才会触发验证角色编码是否存在
	var oldCode = null;
	$scope.editRightModal = function(entity){
		$scope.rightInfo = angular.copy(entity);
		oldCode = entity.rightCode;
		$scope.editCodeCheckFlag = true;
		$("#editRightModal").modal("show");
	}
	//验证编码是否唯一
	$scope.checkCodeUnique = function(){
		$scope.submitting = true;
		$http.post("right/checkCodeUnique.do","rightCode="+$scope.newRightInfo.rightCode,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.status){
					$scope.codeCheckFlag = true;
					$scope.submitting = false;
				} else {
					$scope.codeCheckFlag = false;
					$scope.submitting = false;
					$scope.codeCheckMsg = msg.msg;
				}
			}).error(function(){
			})
	}
	//验证角色编码是否唯一
	$scope.checkEditCodeUnique = function(){
		if(oldCode==$scope.rightInfo.rightCode){
			$scope.editCodeCheckFlag = true;
			return;
		}
		$scope.submitting = true;
		$http.post("right/checkCodeUnique.do","rightCode="+$scope.rightInfo.rightCode,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.status){
					$scope.editCodeCheckFlag = true;
					$scope.submitting = false;
				} else {
					$scope.editCodeCheckFlag = false;
					$scope.editCodeCheckMsg = msg.msg;
					$scope.submitting = false;
				}
			}).error(function(){
			})
	}
	//提交新的角色
	$scope.submitNewRight = function(){
		$scope.submitting = true;
		$http.post("right/saveRight.do",angular.toJson({"right":$scope.newRightInfo}))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#addRightModal").modal("hide");
				$scope.submitting = false;
				$scope.query();
			}).error(function(){
				$socpe.notice("提交失败");
			$scope.submitting = false;
			})
	}
	//提交修改的角色
	$scope.submitEditRight = function(){
		$scope.submitting = true;
		$http.post("right/updateRight.do",angular.toJson({"right":$scope.rightInfo}))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#editRightModal").modal("hide");
				$scope.query();
				$scope.submitting = false;
			}).error(function(){
				$scope.notice("提交失败");
			$scope.submitting = false;
			})
	}
	//用户
	$scope.editUserModal = function(entity){
		$scope.submitting = true;
		$scope.rightInfo = entity;
		$http.post("right/getUsers.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.rightUsers = msg.rightUsers;
				$scope.userGroup = msg.userGroup;
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
	}
	//角色
	$scope.editRoleModal = function(entity){
		$scope.submitting = true;
		$scope.rightInfo = entity;
		$http.post("right/getRoles.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.rightRoles = msg.rightRoles;
				$scope.roleGroup = msg.roleGroup;
				$scope.submitting = false;
			}).error(function(){
			$scope.submitting = false;
			});
		$("#editRoleModal").modal("show");
	}
	//菜单
	$scope.editMenuModal = function(entity){
		$scope.rightInfo = entity;
		$scope.submitting = true;
		$http.post("right/getMenus.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.rightMenus = msg.rightMenus;
				$scope.menuGroup = msg.menuGroup;
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
		$("#editMenuModal").modal("show");
	}
	//提交用户
	$scope.editUser = function(){
		$scope.submitting = true;
		var users = $scope.userGridApi.selection.getSelectedRows();
		var userIds = [];
		angular.forEach(users,function(data,index){
			userIds[index] = data.id;
		});
		var param = {"rightId":$scope.rightInfo.id,"userIds":userIds};
		$http.post("right/editRightUser.do",angular.toJson(param))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#editUserModal").modal("hide");
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
	}
	//提交角色
	$scope.editRole = function(){
		$scope.submitting = true;
		var roles = $scope.roleGridApi.selection.getSelectedRows();
		var roleIds = [];
		angular.forEach(roles,function(data,index){
			roleIds[index] = data.id;
		});
		var param = {"rightId":$scope.rightInfo.id,"roleIds":roleIds};
		$http.post("right/editRightRole.do",angular.toJson(param))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#editRoleModal").modal("hide");
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
	}
	//提交菜单
	$scope.editMenu = function(){
		$scope.submitting = true;
		var menus = $scope.menuGridApi.selection.getSelectedRows();
		var menuIds = [];
		angular.forEach(menus,function(data,index){
			menuIds[index] = data.id;
		});
		var param = {"rightId":$scope.rightInfo.id,"menuIds":menuIds};
		$http.post("right/editRightMenu.do",angular.toJson(param))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#editMenuModal").modal("hide");
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
	}
	//取消
	$scope.cancel = function(){
		$("#addRightModal").modal("hide");
	}
	//取消修改
	$scope.cancelEdit = function(){
		$("#editRightModal").modal("hide");
	}
	$scope.cancelUser = function(){
		$("#editUserModal").modal("hide");
	}
	$scope.cancelRole = function(){
		$("#editRoleModal").modal("hide");
	}
	$scope.cancelMenu = function(){
		$("#editMenuModal").modal("hide");
	}
	$scope.deleteRight=function(id){
        SweetAlert.swal({
            title: "确认删除？",
//            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post("right/deleteRight.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	        		.success(function(msg){
	        			$scope.notice(msg.msg);
	        			$scope.query();
	        		}).error(function(){
	        		});
	            }
        });
    };
	//删除
//	$scope.deleteRight = function(id){
//		$http.post("right/deleteRight.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
//		.success(function(msg){
//			console.log(msg);
//			$scope.notice(msg.msg);
//			$scope.query();
//		}).error(function(){
//			console.log("deleteRight error...");
//		});
//	}
	//清空查询条件
	$scope.resetForm = function(){
		$scope.baseInfo = {};
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});

});