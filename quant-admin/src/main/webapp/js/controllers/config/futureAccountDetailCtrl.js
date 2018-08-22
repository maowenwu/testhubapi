/**
 * 配置中心-期货用户详情
 */
angular.module('inspinia', ['uiSwitch']).controller('futureAccountDetailCtrl', function ($scope, $http, $state, $stateParams, i18nService, SweetAlert, $document) {
    i18nService.setCurrentLang('zh-cn');
    $scope.baseInfo = null;
    $scope.positionInfo = null;
    $scope.orderInfo = null;
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.quanFutureAccount = null;
    $scope.financeInfo = null;
    $scope.orderType = [
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

    $scope.orderSubType = {
        A: [
            {
                value: "-1",
                key: "全部"
            },
            {
                value: "6",
                key: "已成交"
            },
            {
                value: "7",
                key: "已撤销"
            },
            {
                value: "5",
                key: "部分成交已撤销"
            },
            {
                value: "3",
                key: "未成交"
            },
            {
                value: "4",
                key: "部分成交"
            }
        ],
        B: [
            {
                value: "-2",
                key: "全部"
            },
            {
                value: "3",
                key: "未成交"
            },
            {
                value: "4",
                key: "部分成交"
            }
        ],
        C: [
            {
                value: "-3",
                key: "全部"
            },
            {
                value: "6",
                key: "已成交"
            },
            {
                value: "7",
                key: "已撤销"
            },
            {
                value: "5",
                key: "部分成交已撤销"
            },
        ]
    }
    $scope.futureAccountDetailGrid = {
        data: 'futureAccountDetailData',
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
        onRegisterApi: function (gridApi) {
            $scope.futureAccountDetailGrid = gridApi;
        }
    };
    $scope.futureAccountGrid = {
        data: 'futureAccountData',
        useExternalPagination: true,
        enableHorizontalScrollbar: 0,
        enableVerticalScrollbar: 0,
        columnDefs: [
            {field: 'exchangeId', displayName: '交易所id'},
            {field: 'accountSourceId', displayName: '用户id'},
            {field: 'accountName', displayName: '账号名'},
            {
                field: 'id', displayName: '操作', cellTemplate:
                    '<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'futureAccount.update\')"  ng-click="grid.appScope.toProxy()">查看密钥</a></div> '
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.futureAccountGrid = gridApi;
        }
    };
    $scope.positionGrid = {
        data: 'positionData',
        useExternalPagination: true,
        enableHorizontalScrollbar: 1,
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
            {field: 'available', displayName: '可平量（张）'},
            {field: 'updateTime', displayName: '更新时间'}
        ],
        onRegisterApi: function (gridApi) {
            $scope.positionGrid = gridApi;
        }
    };
    $scope.orderGrid = {
        data: 'orderData',
        enablePagination: true,
        useExternalPagination: true,
        paginationPageSize:10,					//分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        enableHorizontalScrollbar: 1,			//横向滚动条
        enableVerticalScrollbar : 1,			//纵向滚动条
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
            {
                field: 'innerOrderId', displayName: '操作', cellTemplate:
                    '<div class="lh30"><a ng-click="grid.appScope.cancelOrder(row.entity)">撤单</a></div>'
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.orderGrid = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.queryOrder($scope.orderInfo);
            });
        }
    };
    $scope.financeGrid = {
        data: 'financeData',
        enableSorting: true,
        enablePagination: true,
        enablePaginationControls: true,
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
        onRegisterApi: function (gridApi) {
            $scope.financeGrid = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.queryFinanceHistory($scope.baseInfo);
            });
        }
    };
    //查询
    $scope.queryAccountInfo = function () {
        $http.post('futureAccountDetail/selectAccount.do', "futureAccount=" + $stateParams.futureAccount, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (entity) {
                if (!entity) {
                    return;
                }
                $scope.futureAccountData = entity;
            }).error(function () {
        });
    }
    $scope.queryAccountInfo();
    $scope.queryAccountDetail = function () {
        $http.post('futureAccountDetail/selectAccountDetail.do', "futureAccount=" + $stateParams.futureAccount , {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (entity) {
                if (!entity) {
                    return;
                }
                $scope.futureAccountDetailData = entity;
                $scope.futureAccountDetailData.forEach(function (item) {
                    item.updateTime = new Date(item.updateTime).format('yyyy-MM-dd HH:mm:ss');
                })
            }).error(function () {
        });
    }
    $scope.queryAccountDetail();
    $scope.queryFinanceHistory = function (entity) {
        $scope.baseInfo = entity;
        $http.post('futureAccountDetail/selectFinanceHistory.do', "futureAccount=" + $stateParams.futureAccount +"&baseInfo="+angular.toJson(entity)
            +"&pageNum=" + $scope.paginationOptions.pageNo + "&pageSize=" +
            $scope.paginationOptions.pageSize, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (page) {
                if (!page) {
                    return;
                }
                $scope.financeData = page.list;
                $scope.financeData.forEach(function (item) {
                    item.createTime = new Date(item.createTime).format('yyyy-MM-dd HH:mm:ss');
                    if (item.moneyType == 1) {
                        item.moneyType = '充值';
                    }
                    if (item.moneyType == 2) {
                        item.moneyType = '提现';
                    }
                })
                $scope.financeGrid.totalItems = page.total;
            }).error(function () {
        });
    }
    $scope.queryFinanceHistory($scope.baseInfo);

    $scope.queryPosition = function (entity) {
        $http.post('futureAccountDetail/selectAccountPosition.do', "positionInfo=" + angular.toJson(entity) + "&futureAccount=" + $stateParams.futureAccount +"&pageNum="
            + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (page) {
                if (!page) {
                    return;
                }
                $scope.positionData = page.list;
                $scope.positionData.forEach(function (item) {
                    item.updateTime = new Date(item.updateTime).format('yyyy-MM-dd HH:mm:ss');
                    if (item.offset == 1){
                        item.offset = '多';
                    }
                    if (item.offset == 2){
                        item.offset = '空';
                    }
                })
                $scope.positionGrid.totalItems = page.total;
            }).error(function () {
        });
    }
    $scope.queryOrder = function (entity) {
        $http.post('futureAccountDetail/selectOrder.do', "orderInfo=" + angular.toJson(entity) + "&futureAccount=" + $stateParams.futureAccount + "&pageNum="
            + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (page) {
                if (!page) {
                    return;
                }
                $scope.orderData = page.list;
                $scope.orderGrid.totalItems = page.total;
            }).error(function () {
        });
    }

    $scope.toProxy = function () {
        $state.go('config.proxy');
    }

    //提交新的任务
    $scope.submitFinanceInfo = function (financeInfo) {
        SweetAlert.swal({
                title: "确认添加？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#66CCFF",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.submitting = true;
                    $http.post('futureAccountDetail/insertFinance.do', "financeInfo=" + angular.toJson(financeInfo) + "&futureAccount=" + $stateParams.futureAccount,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (msg) {
                            $scope.notice(msg.msg);
                            $scope.submitting = false;
                            if (msg.status) {
                                $scope.queryFinanceHistory();
                            }
                        }).error(function () {
                        $scope.notice("提交失败");
                        $scope.submitting = false;
                    })
                }
            });
    }
    $scope.deleteInfo = function (entity) {
        SweetAlert.swal({
                title: "确认添加？",
//            text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#56ac31",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("futureAccount/deleteFutureAccount.do", "id=" + entity.id, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (msg) {
                            $scope.notice(msg.msg);
                            $scope.query();
                        }).error(function () {
                        $socpe.notice("删除失败");
                    })
                }
            });
    };
});