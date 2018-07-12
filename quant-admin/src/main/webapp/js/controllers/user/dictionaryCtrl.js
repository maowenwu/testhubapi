/**
 * 用户中心-用户管理 
*/
angular.module('inspinia',['uiSwitch']).controller('dictionaryCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = {status:2};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.dicGrid = {
		data: 'dicData',
		enableSorting: true,
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,
		enableHorizontalScrollbar: 0,
		enableVerticalScrollbar: 0,
		columnDefs: [
            {field: 'sysKey', displayName: '字典键'},
            {field: 'sysName', displayName: '字典名称'},
            {field: 'sysValue', displayName: '字典值'},
//            {field: 'parentName', displayName: '上一级'},
            {field: 'orderNo', displayName: '排序'},
            {field: 'status', displayName: '状态', cellFilter:"formatDropping:"+ angular.toJson($scope.status)},
            {field: 'remark', displayName: '备注'},
            {field: 'id', displayName: '操作', cellTemplate: 
            	 '<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'sysDict.update\')"  ng-click="grid.appScope.editModal(row.entity)">修改</a> | '
                +'<a ng-show="grid.appScope.hasPermit(\'sysDict.delete\')"  ng-click="grid.appScope.deleteInfo(row.entity)">删除</a></div>'
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
		$http.post('sysDict/selectDicByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.dicData = page.result;
					$scope.dicGrid.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.query();
	//增加新的字典
	$scope.addModal = function(){
		$scope.newInfo = {status:1};
		$("#addModal").modal("show");
	}
	//修改字典
	$scope.editModal = function(entity){
		$scope.newInfo = angular.copy(entity);
		$("#addModal").modal("show");
	}
	
	//提交新的字典
	$scope.submitNewInfo = function(){
		$scope.submitting = true;
		$http.post("sysDict/saveSysDict.do",angular.toJson({"info":$scope.newInfo}))
			.success(function(msg){
				$scope.notice(msg.msg);
				$scope.submitting = false;
				if(msg.status){
					$scope.query();
					$("#addModal").modal("hide");
				}
			}).error(function(){
				$socpe.notice("提交失败");
				$scope.submitting = false;
			})
	}
	//取消
	$scope.cancel = function(){
		$("#addModal").modal("hide");
	}
	$scope.deleteInfo=function(entity){
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
	            	$http.post("sysDict/deleteSysDict.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	    			.success(function(msg){
	    				$scope.notice(msg.msg);
	    				$scope.query();
	    			}).error(function(){
	    				$socpe.notice("删除失败");
	    			})
	            }
        });
    };
	//删除
//	$scope.deleteInfo = function(entity){
//		console.log(entity);
//		$http.post("sysDict/deleteSysDict.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
//			.success(function(msg){
//				$scope.notice(msg.msg);
//				$scope.query();
//			}).error(function(){
//				$socpe.notice("删除失败");
//			})
//	}
	//清空查询条件
	$scope.resetForm = function(){
		$scope.baseInfo = {status:2};
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