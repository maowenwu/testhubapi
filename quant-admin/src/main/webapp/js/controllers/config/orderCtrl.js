/**
 * 用户中心-摆单配置管理 
*/
angular.module('inspinia',['uiSwitch']).controller('orderCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = {status:2};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.orderGrid = {
		data: 'orderData',
		enableSorting: true,
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,
		enableHorizontalScrollbar: 0,
		enableVerticalScrollbar: 0,
		columnDefs: [
            {field: 'symbol', displayName: '交易对'},
            {field: 'contractType', displayName: '合约类型'},
            {field: 'placeOrderInterval', displayName: '摆单间隔时间'},
            {field: 'expectYields', displayName: '期望收益率'},
            {field: 'priceStep', displayName: '合并价格'},
            {field: 'minCopyFactor', displayName: '拷贝系数-下限'},
            {field: 'maxCopyFactor', displayName: '拷贝系数-上限'},
            {field: 'maxAmountPerPrice', displayName: '单价格最大委托数量'},
            {field: 'bidsBasisPrice', displayName: '买单基准价格'},
            {field: 'asksBasisPrice', displayName: '卖单基准价格'},
            {field: 'bidsMaxAmount', displayName: '买单挂单数量上限'},
            {field: 'asksMaxAmount', displayName: '卖单挂单数量上限'},
            {field: 'longMaxAmount', displayName: '多仓数量上限'},
            {field: 'shortMaxAmount', displayName: '空仓数量上限'},
            {field: 'maxLongPosition', displayName: '多仓持仓总量上限'},
            {field: 'maxShortPosition', displayName: '空仓持仓总量上限'},
            {field: 'contractMarginReserve', displayName: '合约账户保留保证金'},
            {field: 'spotCoinReserve', displayName: '币币账户保留币量'},
            {field: 'spotBalanceReserve', displayName: '币币账户保留资金'},
            {field: 'stopTime1', displayName: '临近交割停止下开仓单时间'},
            {field: 'stopTime2', displayName: '停止摆盘开始时间'},
            {field: 'id', displayName: '操作', cellTemplate: 
            	'<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'spotJob.update\')"  ng-click="grid.appScope.editModal(row.entity)">修改</a>'
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
		$http.post('order/selectByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.orderData = page.result;
					$scope.orderGrid.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.query();
	//增加新的任务
	$scope.addModal = function(){
		$scope.newInfo = {status:1};
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
		$http.post('order/updateOrder.do',"newInfo=" + angular.toJson($scope.newInfo),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
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