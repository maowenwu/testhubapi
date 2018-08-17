/**
 * 配置中心-期货用户详情
*/
angular.module('inspinia',['uiSwitch']).controller('futureAccountDetailCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = {status:2};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
    var accountId = $stateParams.accountId;
	$scope.futureAccountGrid = {
		data: 'futureAccountData',
		enableSorting: true,
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,
		enableHorizontalScrollbar: 0,
		enableVerticalScrollbar: 0,
		columnDefs: [
            {field: 'exchangeId', displayName: '交易所'},
            {field: 'accountSourceId', displayName: '账户ID'},
            {field: 'accountsType', displayName: '账户名'},
            {field: 'state', displayName: '账号状态'},
            {field: 'id', displayName: '操作', cellTemplate: 
           	 '<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'futureAccount.update\')"  ng-click="grid.appScope.editModal(row.entity)">查看私钥</a> </div> '
            }
        ],
        onRegisterApi: function(gridApi){
        	$scope.gridApi = gridApi;
        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
        		$scope.paginationOptions.pageNo = newPage;
        		$scope.paginationOptions.pageSize = pageSize;
        	});
        }
	};
	$scope.futureAccountDetailGrid = {
		data: 'futureAccountDetailData',
		enableSorting: true,
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,
		enableHorizontalScrollbar: 0,
		enableVerticalScrollbar: 0,
		columnDefs: [
            {field: 'coinType', displayName: '品种'},
            {field: 'marginBalance', displayName: '账户权益'},
            {field: 'marginAvailable', displayName: '可用保证金'},
            {field: 'marginPosition', displayName: '持仓保证金'},
            {field: 'marginFrozen', displayName: '冻结保证金'},
            {field: 'profitUnreal', displayName: '未实现盈亏'},
            {field: 'profitReal', displayName: '已实现盈亏'},
            {field: 'riskRate', displayName: '保证金率'},
            {field: 'liquidationPrice', displayName: '预估爆仓价'},
            {field: 'state', displayName: '更新时间'},
        ],
        onRegisterApi: function(gridApi){
        	$scope.gridApi = gridApi;
        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
        		$scope.paginationOptions.pageNo = newPage;
        		$scope.paginationOptions.pageSize = pageSize;
        	});
        }
	};

	//查询
	$scope.queryAccountInfo = function(){
		$http.post('futureAccountDetail/selectAccount.do',"accountId="+angular.toJson(accountId)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.futureAccountData = page.result;
					$scope.futureAccountGrid.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.queryAccountDetail = function(){
		$http.post('futureAccount/selectAccountDetail.do',"accountId="+angular.toJson(accountId)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.futureAccountDetailData = page.result;
					$scope.futureAccountDetailGrid.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.queryAccountInfo();
	$scope.queryAccountDetail();
	//增加新的任务
	$scope.addModal = function(){
		$("#addModal").modal("show");
	}
	//修改任务
	$scope.editModal = function(entity){
		$scope.newInfo = angular.copy(entity);
		$("#editRoleModal").modal("show");
	}
	
	$scope.submit = function(){
		$http.post('futureAccount/insertFutureAccount.do',"newInfo=" + angular.toJson($scope.addInfo),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
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
	
	//提交新的任务
	$scope.submitNewInfo = function(){
		$scope.submitting = true;
		$http.post('futureAccount/updateFutureAccount.do',"newInfo=" + angular.toJson($scope.newInfo),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
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
	            	$http.post("futureAccount/deleteFutureAccount.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
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