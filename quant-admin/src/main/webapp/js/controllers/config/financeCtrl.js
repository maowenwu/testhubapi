/**
 * 配置中心-账号财务管理 
*/
angular.module('inspinia',['uiSwitch']).controller('financeCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = {status:2};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.financeGrid = {
		data: 'financeData',
		enableSorting: true,
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,
		enableHorizontalScrollbar: 0,
		enableVerticalScrollbar: 0,
		columnDefs: [
            {field: 'exchangeId', displayName: '交易所id'},
            {field: 'accountId', displayName: '用户id'},
            {field: 'coinType', displayName: '币类'},
            {field: 'transferAmount', displayName: '金额'},
            {field: 'moneyType', displayName: '方式'},
            {field: 'createTime', displayName: '创建时间'},
            {field: 'updateTime', displayName: '更新时间'},
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
		$http.post('finance/selectByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.financeData = page.result;
					$scope.financeGrid.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.query();
	//增加新的任务
	$scope.addModal = function(){
		$("#editRoleModal").modal("show");
	}
	//修改任务
	$scope.editModal = function(entity){
		$scope.newInfo = angular.copy(entity);
		$("#editRoleModal").modal("show");
	}
	
	//提交新的任务
	$scope.submitNewInfo = function(){
		$scope.submitting = true;
		$http.post('finance/insertFinance.do',"newInfo=" + angular.toJson($scope.newInfo),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.notice(msg.msg);
				$scope.submitting = false;
				if(msg.status){
					$scope.query();
					$("#editRoleModal").modal("hide");
				}
			}).error(function(){
				$socpe.notice("提交失败");
				$scope.submitting = false;
			})
	}
	//取消
	$scope.cancel = function(){
		$("#editRoleModal").modal("hide");
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
	            	$http.post("spotJob/deleteSpotJob.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	    			.success(function(msg){
	    				$scope.notice(msg.msg);
	    				$scope.query();
	    			}).error(function(){
	    				$socpe.notice("删除失败");
	    			})
	            }
        });
    };
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