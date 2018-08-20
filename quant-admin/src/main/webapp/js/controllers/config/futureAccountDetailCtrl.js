/**
 * 配置中心-期货用户详情
*/
angular.module('inspinia',['uiSwitch']).controller('futureAccountDetailCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = null;
	$scope.positionInfo = null;
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.quanFutureAccount = null;
    $scope.firstClass = [
        {
            value: "A",
            key: "全部"
        },
        {
            value: "B",
            key: "当前委托",
        },
        {
            value: "C",
            key: "历史委托",
        }
    ];

    $scope.secondClass={
        A:[
            {
                value:"A1",
                key:"全部"
            },
            {
                value:"A2",
                key:"已成交"
            },
            {
                value:"A3",
                key:"已撤销"
            },
            {
                value:"A4",
                key:"部分成交已撤销"
            },
            {
                value:"A5",
                key:"未成交"
            },
            {
                value:"A6",
                key:"部分成交"
            }
        ],
        B:[
            {
                value:"B1",
                key:"全部"
            },
            {
                value:"B2",
                key:"未成交"
            },
            {
                value:"B2",
                key:"部分成交"
            }
        ],
        C:[
            {
                value:"C1",
                key:"全部"
            },
            {
                value:"C2",
                key:"已成交"
            },
            {
                value:"C3",
                key:"已撤销"
            },
            {
                value:"C4",
                key:"部分成交已撤销"
            },
        ]
    }
	$scope.futureAccountDetailGrid = {
		data: 'futureAccountDetailData',
        useExternalPagination: true,
		enableHorizontalScrollbar: 0,
		enableVerticalScrollbar: 1,
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
            {field: 'updateTime', displayName: '更新时间'},
        ],
        onRegisterApi: function(gridApi){
        	$scope.futureAccountDetailGrid = gridApi;
        }
	};
    $scope.futureAccountGrid = {
        data: 'futureAccountData',
        useExternalPagination: true,
        enableHorizontalScrollbar: 0,
        enableVerticalScrollbar: 1,
        columnDefs: [
            {field: 'exchangeId', displayName: '交易所id'},
            {field: 'accountSourceId', displayName: '用户id'},
            {field: 'accountName', displayName: '账号名'},
            {field: 'id' , displayName: '操作', cellTemplate:
                    '<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'futureAccount.update\')"  ng-click="grid.appScope.toProxy()">查看密钥</a></div> '
            }
        ],
        onRegisterApi: function(gridApi){
            $scope.futureAccountGrid = gridApi;
        }
    };
    $scope.positionGrid = {
        data: 'positionData',
        useExternalPagination: true,
        enableHorizontalScrollbar: 0,
        enableVerticalScrollbar: 1,
        columnDefs: [
            {field: 'contractCode', displayName: '合约代码'},
            {field: 'leverRate', displayName: '杠杆倍数'},
            {field: 'contractType', displayName: '合约类型'},
            {field: 'offset', displayName: '合约方向'},
            {field: 'amount', displayName: '持仓量（张）'},
            {field: 'costOpen', displayName: '开仓均价($)'},
            {field: 'costHold', displayName: '持仓均价 ($)'},
            {field: 'profitUnreal', displayName: '未实现盈亏 (฿)'},
            {field: 'contractType', displayName: '可平量（张）'},
            {field: 'updateTime', displayName: '更新时间'}
        ],
        onRegisterApi: function(gridApi){
            $scope.positionGrid = gridApi;
        }
    };
    $scope.orderGrid = {
        data: 'orderData',
        useExternalPagination: true,
        enableHorizontalScrollbar: 0,
        enableVerticalScrollbar: 1,
        columnDefs: [
            {field: 'innerOrderId', displayName: '内部订单号'},
            {field: 'exOrderId', displayName: '交易所订单号'},
            {field: 'linkOrderId', displayName: '关联订单号'},
            {field: 'contractCode', displayName: '合约代码'},
            {field: 'offset', displayName: '开平方向'},
            {field: 'side', displayName: '买卖方向'},
            {field: 'lever', displayName: '杠杆'},
            {field: 'createDate', displayName: '委托时间'},
            {field: 'orderQty', displayName: '委托数量 (张)'},
            {field: 'orderPrice', displayName: '委托价格 ($)'},
            {field: 'dealPrice', displayName: '成交均价 ($)'},
            {field: 'marginFrozen', displayName: '冻结保证金 ($)'},
            {field: 'status', displayName: '状态'},
            {field: 'innerOrderId' , displayName: '操作', cellTemplate:
                    '<div class="lh30"><a ng-click="grid.appScope.cancelOrder(row.entity)">撤单</a></div>'
            }
        ],
        onRegisterApi: function(gridApi){
            $scope.orderGrid = gridApi;
        }
    };
    $scope.financeGrid = {
        data: 'financeData',
        enableSorting: true,
        paginationPageSize: 10,
        paginationPageSizes: [10, 20, 50, 100],
        useExternalPagination: true,
        enableHorizontalScrollbar: 0,
        enableVerticalScrollbar: 0,
        columnDefs: [
            {field: 'coinType', displayName: '币类'},
            {field: 'transferAmount', displayName: '金额'},
            {field: 'moneyType', displayName: '方式'},
            {field: 'createTime', displayName: '时间'}
        ],
        onRegisterApi: function(gridApi){
            $scope.financeGrid = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
            });
        }
    };
	//查询
	$scope.queryAccountInfo = function(){
		$http.post('futureAccountDetail/selectAccount.do',"futureAccount="+$stateParams.futureAccount+"&pageNum="+$scope.paginationOptions.pageNo+"&pageSize="+
            $scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
                    if(!page){
                        return;
                    }
                    $scope.futureAccountData = page.list;
                    $scope.futureAccountGrid.totalItems = page.total;
				}).error(function(){
				});
	}
    $scope.queryAccountInfo();
	$scope.queryAccountDetail = function(){
        $http.post('futureAccountDetail/selectAccountDetail.do',"futureAccount="+$stateParams.futureAccount+"&pageNum="+$scope.paginationOptions.pageNo+"&pageSize="+
            $scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.futureAccountDetailData = page.list;
                    $scope.futureAccountDetailData.forEach(function(item){
                        item.updateTime=new Date(item.updateTime).format('yyyy-MM-dd HH:mm:ss');
                    })
					$scope.futureAccountDetailGrid.totalItems = page.total;
				}).error(function(){
				});
	}
	$scope.queryAccountDetail();
	$scope.queryPosition = function(){
        $http.post('futureAccountDetail/selectAccountPosition.do',"positionInfo="+angular.toJson($scope.positionInfo)+"&pageNum="+$scope.paginationOptions.pageNo+"&pageSize="+
            $scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.positionData = page.list;
                   	$scope.positionData.forEach(function(item){
                        item.updateTime=new Date(item.updateTime).format('yyyy-MM-dd HH:mm:ss');
                    })
					$scope.positionGrid.totalItems = page.total;
				}).error(function(){
				});
	}
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

	$scope.toProxy = function(){
        $state.go('config.proxy');
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