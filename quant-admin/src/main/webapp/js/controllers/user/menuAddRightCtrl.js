/**
 * 用户中心-用户管理 
*/
angular.module('inspinia').controller('menuAddRightCtrl',function($scope,$http,$state,$stateParams,i18nService){
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = {};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.paginationOptions.pageSize = 500;
	$scope.rightGrid = {
		data: 'rightData',
		enableSorting: true,
		paginationPageSize: 500,
		paginationPageSizes: [500],
		useExternalPagination: true,
		enableHorizontalScrollbar: 1,
		enableVerticalScrollbar: 1,
		columnDefs: [
            {field: 'id', displayName: '权限ID'},
            {field: 'rightName', displayName: '权限名称'},
            {field: 'rightCode',  displayName: '权限编码'},
            {field: 'rightType', displayName: '权限类型'}
//          {field: 'status', displayName: '状态', cellFilter:"formatDropping:"+ angular.toJson($scope.status)},
        ],
        onRegisterApi: function(gridApi){
        	$scope.gridApi = gridApi;
//        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
//        		$scope.paginationOptions.pageNo = newPage;
//        		$scope.paginationOptions.pageSize = pageSize;
//        		$scope.query();
//        	});
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
	$scope.baseInfo.id = $stateParams.id;
	//查询
	$scope.query = function(){
		$http.post('menu/selectRightByMenu.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(msg){
					if(!msg){
						return;
					}
					$scope.rightData = msg.page.result;
					$scope.rightGrid.totalItems = msg.page.totalCount;
					$scope.menuRights = msg.menuRights;
				}).error(function(){
				});
	}
	$scope.query();
	
	//提交权限
	$scope.editMenu = function(){
		$scope.submitting = true;
		var rights = $scope.gridApi.selection.getSelectedRows();
		var rightIds = [];
		angular.forEach(rights,function(data,index){
			rightIds[index] = data.id;
		});
		var param = {"menuId":$stateParams.id,"rightIds":rightIds};
		$http.post("menu/editMenuRight.do",angular.toJson(param))
			.success(function(msg){
				$scope.notice(msg.msg);
				$state.transitionTo('user.menu',null,{reload:true});
				$scope.submitting = true;
			}).error(function(){
				$scope.submitting = true;
			});
	}
	//清空查询条件
	$scope.resetForm = function(){
		$scope.baseInfo = {};
	}
});