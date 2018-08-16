/**
 * 配置中心-代理IP配置管理 
*/
angular.module('inspinia',['uiSwitch']).controller('proxyCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = {status:2};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.proxyGrid = {
		data: 'proxyData',
		enableSorting: true,
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,
		enableHorizontalScrollbar: 0,
		enableVerticalScrollbar: 0,
		columnDefs: [
            {field: 'host', displayName: '主机地址1'},
            {field: 'port', displayName: '端口号'},
            {field: 'userName', displayName: '用户名'},
            {field: 'password', displayName: '用户密码'},
            {field: 'state', displayName: '代理状态'},
            {field: 'updateTime', displayName: '更新时间'},
            {field: 'createTime', displayName: '创建时间'},
            {field: 'id', displayName: '操作', cellTemplate: 
           	 '<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'futureAccount.update\')"  ng-click="grid.appScope.editModal(row.entity)">修改</a> | '
               +'<a ng-show="grid.appScope.hasPermit(\'futureAccount.delete\')"  ng-click="grid.appScope.deleteInfo(row.entity)">删除</a></div>'
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
		$http.post('proxy/selectByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNum="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					debugger;
					if(!page){
						return;
					}

					$scope.proxyData = page.list;
					$scope.proxyGrid.totalItems = page.total;
				}).error(function(){
				});
	}
	$scope.query();
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
		$http.post('proxy/insertProxy.do',"newInfo=" + angular.toJson($scope.addInfo),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
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
		$http.post('proxy/updateProxy.do',"newInfo=" + angular.toJson($scope.newInfo),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
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
	            	$http.post("proxy/deleteProxy.do","id="+entity.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
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