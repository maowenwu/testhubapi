/**
 * 用户中心-菜单管理
*/
angular.module('inspinia').controller('menuCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.baseInfo = {};
	$scope.menuType = [{text:"菜单",value:"menu"},{text:"页面操作",value:"page"}];
	$scope.levelType = [{text:"0",value:0},{text:"1",value:1},{text:"2",value:2}];
	$scope.menuTypeAll = [{text:"全部",value:""},{text:"菜单",value:"menu"},{text:"页面操作",value:"page"}];
	$scope.menuData=[];
	$scope.type=1;
	$scope.menuGrid = {
		data: 'menuData',
		paginationPageSize: 500,
		paginationPageSizes: [500, 1000],
		useExternalPagination: true,		  	//开启拓展名
		columnDefs: [
            {field: 'id',width: 100, displayName: '菜单ID'},
            {field: 'menuName',width: 200, displayName: '菜单名称'},
            {field: 'menuCode',/*cellClass: 'align-left',*/width: 200, displayName: '菜单编码'},
            {field: 'menuUrl',/*cellClass: 'align-left',*/width: 200, displayName: '菜单路径'},
            {field: 'menuLevel',width: 100, displayName: '菜单等级'},
            {field: 'menuType',width: 200, displayName: '菜单类型', cellFilter:"formatDropping:"+ angular.toJson($scope.menuType)},
            {field: 'parentId',width: 200, displayName: '上级ID'},
            {field: 'orderNo',width: 200,/*cellClass: 'align-left',*/ displayName: '排序'},
            {field: 'options',width: 200,pinnedRight:true, displayName: '操作', cellTemplate:
            	'<div class="lh30"><a ng-click="grid.appScope.editUserModal(row.entity)">修改</a> | '
//               +'<a ui-sref="user.menuAddRight({id:row.entity.id})">权限</a> | '
               +'<a ng-click="grid.appScope.deleteMenu(row.entity.id)">删除</a></div>'
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
		$http.post('menu/selectMenuByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize=1000"
			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.menuData = page.result;
					$scope.menuGrid.totalItems = page.totalCount;
					angular.forEach($scope.menuData,function(data){
						if(data.menuType=="menu" && data.menuLevel==0){
							data.$$treeLevel = 0;
						} else if(data.menuType=="menu" && data.menuLevel==1){
							data.$$treeLevel = 1;
						}
						if(data.orderNo!=null){
							if(data.menuLevel==0){
								data.orderNo = data.orderNo/1000000;
							}
							else if(data.menuLevel==1){
								data.orderNo = data.orderNo.substr(data.orderNo.length-5,2);
							} else if(data.menuLevel==2){
								data.orderNo = data.orderNo.substr(data.orderNo.length-3,3);
							}
						}
					});

				}).error(function(){
			});
	}
	$scope.query();
	
	//增加新的菜单
	$scope.menuModal = function(){
		$scope.newMenuInfo = {status:1};
		$scope.type=1;
		$("#menuModal").modal("show");
	}
	//修改菜单
	$scope.editUserModal = function(entity){
		$scope.newMenuInfo = angular.copy(entity);
//		if($scope.newMenuInfo.orderNo!=null){
//			$scope.newMenuInfo.orderNo = $scope.newMenuInfo.orderNo.substr($scope.newMenuInfo.orderNo.length-2,2);
//		}
		$scope.type=2;
		$("#menuModal").modal("show");
	}
	$scope.deleteMenu=function(id){
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
	            	$http.post("menu/deleteMenu.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	        		.success(function(msg){
	        			$scope.notice(msg.msg);
	        			$scope.query();
	        		}).error(function(){
	        		});
	            }
        });
    };
//	$scope.deleteMenu = function(id){
//		$http.post("menu/deleteMenu.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
//		.success(function(msg){
//			$scope.notice(msg.msg);
//			$scope.query();
//		}).error(function(){
//		});
//	}
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
			$("#menuModal").modal("hide");
			$scope.notice(msg.msg);
			$scope.query();
			$scope.submitting = false;
		}).error(function(){
			$scope.submitting = false;
		});
	}
	$scope.cancel=function(){
		$("#menuModal").modal("hide");
	}
	
	//权限
	$scope.editRightModal = function(entity){
		$scope.submitting = true;
		$scope.menuInfo = entity;
		$http.post("menu/getRights.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.menuRights = msg.menuRights;
				$scope.rightGroup = msg.rightGroup;
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
		$("#editRightModal").modal("show");
	}
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
				if($scope.menuRights != null){
					for(var i=0;i<$scope.menuRights.length;i++){
						 if(row.entity.id==$scope.menuRights[i].id){
							 row.grid.api.selection.selectRow(row.entity);
						 }
					}
				}
	        },
		};
	//编辑权限
	$scope.editRight = function(){
		var rights = $scope.rightGridApi.selection.getSelectedRows();
		var rightIds = [];
		angular.forEach(rights,function(data,index){
			rightIds[index] = data.id;
		});
		$scope.submitting = true;
		var param = {"menuId":$scope.menuInfo.id,"rightIds":rightIds};
		$http.post("menu/editMenuRight.do",angular.toJson(param))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#editRightModal").modal("hide");
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
	}
	$scope.cancelRight = function(){
		$("#editRightModal").modal("hide");
	}
	//清空查询条件
	$scope.resetForm = function(){
		$scope.baseInfo = {};
	}
	
});