/**
 * 用户中心-角色管理 
*/
angular.module('inspinia',['uiSwitch']).controller('roleCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = {roleStatus:2};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.roleGrid = {
		data: 'roleData',
		enableSorting: true,
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,
		enableHorizontalScrollbar: 0,
		enableVerticalScrollbar: 0,
		columnDefs: [
            {field: 'roleName', displayName: '角色名称'},
            {field: 'roleCode', displayName: '角色编码'},
            {field: 'roleStatus', displayName: '状态', cellFilter:"formatDropping:"+ angular.toJson($scope.status)},
            {field: 'roleRemark', displayName: '备注'},
            {field: 'options', displayName: '操作', cellTemplate: 
            	 '<div class="lh30"><a ng-click="grid.appScope.editRoleModal(row.entity)">修改</a> | '
                +'<a ng-click="grid.appScope.editUserModal(row.entity)">用户</a> | '
                +'<a ng-click="grid.appScope.editRightModal(row.entity)">权限</a> | '
                +'<a ng-click="grid.appScope.deleteRole(row.entity.id)">删除</a></div>'
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
	
	//查询
	$scope.query = function(){
		$http.post('role/selectRoleByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.roleData = page.result;
					$scope.roleGrid.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.query();
	
	//增加新的角色
	$scope.addRoleModal = function(){
		$scope.newRoleInfo = {roleStatus:1};
		$("#addRoleModal").modal("show");
	}
	//修改新的角色
	//备份角色编码，如果修改时发生改变，才会触发验证角色编码是否存在
	var oldCode = null;
	$scope.editRoleModal = function(entity){
		$scope.roleInfo = angular.copy(entity);
		oldCode = entity.roleCode;
		$scope.editCodeCheckFlag = true;
		$("#editRoleModal").modal("show");
	}
	//验证角色编码是否唯一
	$scope.checkCodeUnique = function(){
		$scope.submitting = true;
		$http.post("role/checkCodeUnique.do","roleCode="+$scope.newRoleInfo.roleCode,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.status){
					$scope.codeCheckMsg = msg.msg;
					$scope.submitting = false;
				}
			}).error(function(){
				$scope.submitting = false;
			})
	}
	//提交新的用户
	$scope.submitNewRole = function(){
		$scope.submitting = true;
		$http.post("role/saveRole.do",angular.toJson({"role":$scope.newRoleInfo}))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#addRoleModal").modal("hide");
				$scope.submitting = false;
				$scope.query();
			}).error(function(){
				$socpe.notice("提交失败");
				$scope.submitting = false;
			})
	}
	//提交修改的用户
	$scope.submitEditRole = function(){
		//用户名可以使用时，允许提交
		$scope.submitting = true;
		if(!$scope.editCodeCheckFlag){
			$scope.submitting = false;
			return;
		}

		$http.post("role/updateRole.do",angular.toJson({"role":$scope.roleInfo}))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#editRoleModal").modal("hide");
				$scope.submitting = false;
				$scope.query();
			}).error(function(){
				$socpe.notice("提交失败");
			$scope.submitting = false;
			})
	}
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
				if($scope.roleUsers != null){
					for(var i=0;i<$scope.roleUsers.length;i++){
						 if(row.entity.id==$scope.roleUsers[i].id){
							 row.grid.api.selection.selectRow(row.entity);
						 }
					}
				}
	        },
		};
	//权限表
	$scope.rightGrid = {
			data: 'rightGroup',
			enableHorizontalScrollbar: 0,
			enableVerticalScrollbar: 1,
			useExternalPagination: true,
			columnDefs: [
	            {field: 'id', width:207,pinnable: false,sortable: false, displayName: 'ID'},
	            {field: 'rightName', width:207,pinnable: false,sortable: false, displayName: '权限名称'},
	            {field: 'rightCode',  width:207,pinnable: false,sortable: false, displayName: '权限编码'},
	            {field: 'rightComment', width:207,pinnable: false,sortable: false, displayName: '备注'}
	        ],
	        onRegisterApi: function(gridApi){
	        	$scope.rightGridApi = gridApi;
	        },
	        isRowSelectable: function(row){ // 选中行 
				if($scope.roleRights != null){
					for(var i=0;i<$scope.roleRights.length;i++){
						 if(row.entity.id==$scope.roleRights[i].id){
							 row.grid.api.selection.selectRow(row.entity);
						 }
					}
				}
	        },
		};
	//用户
	$scope.editUserModal = function(entity){
		$scope.submitting = true;
		$scope.roleInfo = entity;
		$http.post("role/getUsers.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.roleUsers = msg.roleUsers;
				$scope.userGroup = msg.userGroup;
				$scope.submitting = false;
			}).error(function(){
			$scope.submitting = false;
			});
		$("#editUserModal").modal("show");
	}
	//权限
	$scope.editRightModal = function(entity){
		$scope.submitting = true;
		$scope.roleInfo = entity;
		$http.post("role/getRights.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.roleRights = msg.roleRights;
				$scope.rightGroup = msg.rightGroup;
				$scope.submitting = false;
			}).error(function(){
			$scope.submitting = false;
			});
		$("#editRightModal").modal("show");
	}
	//编辑用户
	$scope.editUser = function(){
		var users = $scope.userGridApi.selection.getSelectedRows();
		var userIds = [];
		angular.forEach(users,function(data,index){
			userIds[index] = data.id;
		});
		var param = {"roleId":$scope.roleInfo.id,"userIds":userIds};
		$scope.submitting = true;
		$http.post("role/editRoleUser.do",angular.toJson(param))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#editUserModal").modal("hide");
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
	}
	//编辑权限
	$scope.editRight = function(){
		var rights = $scope.rightGridApi.selection.getSelectedRows();
		var rightIds = [];
		angular.forEach(rights,function(data,index){
			rightIds[index] = data.id;
		});
		var param = {"roleId":$scope.roleInfo.id,"rightIds":rightIds};
		$scope.submitting = true;
		$http.post("role/editRoleRight.do",angular.toJson(param))
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
		$("#addRoleModal").modal("hide");
	}
	//取消
	$scope.cancelEdit = function(){
		$("#editRoleModal").modal("hide");
	}
	$scope.cancelUser = function(){
		$("#editUserModal").modal("hide");
	}
	$scope.cancelRight = function(){
		$("#editRightModal").modal("hide");
	}
	$scope.deleteRole=function(id){
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
	            	$http.post("role/deleteRole.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	        		.success(function(msg){
	        			$scope.notice(msg.msg);
	        			$scope.query();
	        		}).error(function(){
	        		});
	            }
        });
    };
	//清空查询条件
	$scope.resetForm = function(){
		$scope.baseInfo = {roleStatus:2};
	}
	
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
		};
		$scope.refreshTree = function(){
			flags[1] = false;
			$http.get("sysAction/menuTree.do").success(function(data) {
				var parents = [];
				function formatSysMenuData(data) {
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
					return data;
				}
				angular.forEach(data, formatSysMenuData);
				$scope.sysMenus = data;
				$scope.currMenu = data[0];
				if(!$scope.currMenu.state)
					$scope.currMenu.state = {};
				$scope.currMenu.state.selected=true;
				flags[1] = true;
				$scope.refreshPermits();
				$scope.reCreateTree();
			});
		};
		$scope.changedCB = function(ev, data) {
			if (data.action != "select_node")
				return;
			flags[1] = false;
			$scope.$apply(function() {
				var item = data.node.original;
				$scope.currMenu = item;
				flags[1] = true;
				$scope.refreshPermits();
			});
		};
		
		$scope.refreshPermits = function(){
			if(!flags[0] || !flags[1])
				return;
			if(!$scope.currRole.id || !$scope.currMenu.id)
				return;
			$log.info('refreshPermits');
			var param = {menuId: $scope.currMenu.id, roleId: 'role:'+$scope.currRole.id};
			$http({  
			   method:'post',  
			   url: "roleAction/findRolePrivilege.do",  
			   data: $httpParamSerializerJQLike(param),  
			   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},  
			}).success(function(data){
				param.permits = data;
				$scope.permitBlock = param;
				//$scope.permitBlock = {roleId: $scope.currRole.id, menuId: $scope.currMenu.id, permits: [{id:1,text:'aaa',val:true},{id:2,text:'bbb',val:false}]};
			});
		};
		
		$scope.savePermits = function(){
			if(!$scope.currRole.id || !$scope.currMenu.id || !$scope.permitBlock)
				return;
			var param = {roleId: $scope.permitBlock.roleId,menuId: $scope.permitBlock.menuId,rigthCode:[]};
			angular.forEach($scope.permitBlock.permits, function(data){
				if(data.state.selected){
					param.rigthCode.push(data.rigthCode);
				}
			});
			param.rigthCode = param.rigthCode.join(',');
			$scope.saveItem({
				url: "roleAction/saveRoleRigth.do",
				data: param,
				title: '保存权限',
				successCB: function(){
				}
			});
		};
		
		$scope.refreshTree();
	
});