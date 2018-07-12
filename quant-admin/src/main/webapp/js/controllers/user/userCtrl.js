/**
 * 用户中心-用户管理 
*/
angular.module('inspinia',['uiSwitch']).controller('userCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = {};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.userGrid = {
		data: 'userData',
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条 
		columnDefs: [
            {field: 'userName',width: 225, displayName: '用户名'},
            {field: 'realName',width: 225, displayName: '真实姓名'},
            {field: 'email',width: 225, displayName: '邮箱'},
            {field: 'telNo',width: 225, displayName: '电话号码'},
            {field: 'status',width: 225, displayName: '状态', cellFilter:"formatDropping:"+ angular.toJson($scope.status)},
            {field: 'options',width: 228, displayName: '操作',pinnedRight:true, cellTemplate:
            	'<div class="lh30" ng-hide="row.entity.userName==\'admin\'&&grid.appScope.principal.username!=\'admin\'"><a ng-show="grid.appScope.hasPermit(\'user.update\')" ng-click="grid.appScope.editUserModal(row.entity)">修改</a>'
            	 +'<a ng-show="grid.appScope.hasPermit(\'user.delete\')" ng-click="grid.appScope.editRightModal(row.entity)"> | 角色</a>'
               +'<a ng-show="grid.appScope.hasPermit(\'user.update\') && row.entity.id!=1" ng-click="grid.appScope.deleteUser(row.entity.id)"> | 删除</a></div>'
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
	
	$scope.roleGrid = {
			data: 'roleGroup',
			useExternalPagination: true,
			enableHorizontalScrollbar: 0,
			enableVerticalScrollbar: 1,
			columnDefs: [
	            {field: 'id',width:207,pinnable: false,sortable: false, displayName: 'ID'},
	            {field: 'roleName', width:207,pinnable: false,sortable: false, displayName: '角色名称'},
	            {field: 'roleCode', width:207,pinnable: false,sortable: false, displayName: '角色编码'},
	            {field: 'roleRemark', width:377,pinnable: false,sortable: false, displayName: '备注'}
	        ],
	        onRegisterApi: function(gridApi){
	        	$scope.roleGridApi = gridApi;
	        },
	        isRowSelectable: function(row){ // 选中行 
				if($scope.userRoles != null){
					for(var i=0;i<$scope.userRoles.length;i++){
						 if(row.entity.id==$scope.userRoles[i].id){
							 row.grid.api.selection.selectRow(row.entity);
						 }
					}
				}
	        },
		};
	
	$scope.rightGrid = {
			data: 'rightGroup',
			enableHorizontalScrollbar: 0,
			enableVerticalScrollbar: 1,
			useExternalPagination: true,
			columnDefs: [
	            {field: 'id', width:207,pinnable: false,sortable: false, displayName: 'ID'},
	            {field: 'rightName', width:207,pinnable: false,sortable: false, displayName: '角色名称'},
//	            {field: 'rightCode',  width:207,pinnable: false,sortable: false, displayName: '角色编码'},
	            {field: 'rightComment', width:377,pinnable: false,sortable: false, displayName: '备注'}
	        ],
	        onRegisterApi: function(gridApi){
	        	$scope.rightGridApi = gridApi;
	        },
	        isRowSelectable: function(row){ // 选中行 
				if($scope.userRights != null){
					for(var i=0;i<$scope.userRights.length;i++){
						 if(row.entity.id==$scope.userRights[i].id){
							 row.grid.api.selection.selectRow(row.entity);
						 }
					}
				}
	        },
		};
	
	//查询
	$scope.query = function(){
		$http.post('user/selectUserByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.userData = page.result;
					$scope.userGrid.totalItems = page.totalCount;
				}).error(function(){
				});
	}
    $scope.userData = [];
	// $scope.query();
	
	//增加新的用户
	$scope.addUserModal = function(){
		$scope.newUserInfo = {status:1};
		$("#addUserModal").modal("show");
	}
	//修改用户
	$scope.editUserModal = function(entity){
		$scope.userInfo = angular.copy(entity);
		$("#editUserModal").modal("show");
	}
	//修改角色
	$scope.editRoleModal = function(entity){
		$scope.userInfo = entity;
		$scope.submitting = true;
		$http.post("user/getRoles.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.userRoles = msg.userRoles;
				$scope.roleGroup = msg.roleGroup;
				$scope.role = $scope.roleGroup[0];
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
		$("#editRoleModal").modal("show");
	}
	//修改角色
	$scope.editRightModal = function(entity){
		$scope.userInfo = entity;
		$scope.submitting = true;
		$http.post("user/getRights.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.userRights = msg.userRights;
				$scope.rightGroup = msg.rightGroup;
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
		$("#editRightModal").modal("show");
	}
	//验证用户名是否唯一
	$scope.checkNameUnique = function(){
		$scope.submitting = true;
		$http.post("user/checkNameUnique.do","userName="+$scope.newUserInfo.userName,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.status){
					$scope.userNameCheckFlag = true;
					$scope.submitting = false;
				} else {
					$scope.userNameCheckFlag = false;
					$scope.userNameCheckMsg = msg.msg;
					$scope.submitting = false;
				}
			}).error(function(){
			})
	}
	//提交新的用户
	$scope.submitNewUser = function(){
		//用户名可以使用时，允许提交
		$scope.submitting = true;
		if(!$scope.userNameCheckFlag){
			$scope.submitting = false;
			return;
		}
		$http.post("user/saveUser.do",angular.toJson({"user":$scope.newUserInfo}))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#addUserModal").modal("hide");
				$scope.query();
				$scope.submitting = false;
			}).error(function(){
				$socpe.notice("提交失败");
				$scope.submitting = false;
			})
	}
	//提交修改的用户
	$scope.submitEditUser = function(){
		//用户名可以使用时，允许提交
		$scope.submitting = true;
		$http.post("user/updateUser.do",angular.toJson({"user":$scope.userInfo}))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#editUserModal").modal("hide");
				$scope.query();
				$scope.submitting = false;
			}).error(function(){
				$socpe.notice("修改失败");
				$scope.submitting = false;
			})
	}
	//编辑用户角色
	$scope.editRole = function(){
		var roles = $scope.roleGridApi.selection.getSelectedRows();
		var roleIds = [];
		angular.forEach(roles,function(data,index){
			roleIds[index] = data.id;
		});
		var param = {"userId":$scope.userInfo.id,"roleIds":roleIds};
		$scope.submitting = true;
		$http.post("user/editUserRole.do",angular.toJson(param))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#editRoleModal").modal("hide");
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
	}
	//编辑用户角色
	$scope.editRight = function(){
		var rights = $scope.rightGridApi.selection.getSelectedRows();
		var rightIds = [];
		angular.forEach(rights,function(data,index){
			rightIds[index] = data.id;
		});
		var param = {"userId":$scope.userInfo.id,"rightIds":rightIds};
		$scope.submitting = true;
		$http.post("user/editUserRight.do",angular.toJson(param))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#editRightModal").modal("hide");
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
	}
	//取消
	$scope.cancel = function(){
		$("#addUserModal").modal("hide");
	}
	$scope.cancelEdit = function(){
		$("#editUserModal").modal("hide");
	}
	$scope.cancelRole = function(){
		$("#editRoleModal").modal("hide");
	}
	$scope.cancelRight = function(){
		$("#editRightModal").modal("hide");
	}
	$scope.deleteUser=function(id){
        SweetAlert.swal({
            title: "确认删除？",
//            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post("user/deleteUser.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	        		.success(function(msg){
	        			$scope.notice(msg.msg);
	        			$scope.query();
	        		}).error(function(){
	        		});
	            }
        });
    };
    
    //密码重置
    $scope.restPwd=function(id){
        SweetAlert.swal({
            title: "确认密码重置？",
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
	            	$http.post("user/restPwd.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	        		.success(function(msg){
	        			$scope.notice(msg.msg);
	        			$scope.query();
	        		}).error(function(){
	        		});
	            }
        });
    };
//	$scope.deleteUser = function(id){
//		$http.post("user/deleteUser.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
//		.success(function(msg){
//			console.log(msg);
//			$scope.notice(msg.msg);
//			$scope.query();
//		}).error(function(){
//			console.log("addUserRight error...");
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