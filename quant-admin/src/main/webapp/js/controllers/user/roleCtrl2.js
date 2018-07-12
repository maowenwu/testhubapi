/**
 * 用户中心-角色管理 
*/
angular.module('inspinia').controller('roleCtrl2',function($scope,$http,$state,$stateParams,i18nService,SweetAlert){
	var flags = [false, false];
	$scope.rightId = $stateParams.id;	//当前的角色ID
	$scope.rightName =  $stateParams.rightName; // 当前角色的名称
	$scope.allPages = [];				//所有的页面操作
	$scope.allPagesMap = {};
	$scope.showPages = [];				//当前显示菜单所对应的页面操作
	$scope.rightMenus = [];				//用户角色所拥有的菜单
	$scope.rightMenusMap = {};
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
		$http.get("menu/selectMenuAndChildren?id="+$scope.rightId).success(function(data) {
			$scope.rightMenus = data.rightMenus;
			angular.forEach($scope.rightMenus, function(data){
				$scope.rightMenusMap[data.id] = data;
			});
			$scope.allPages = data.allPages;
			var parents = [];
			function formatSysMenuData(data) {
				$scope.allPages.unshift(data);
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
			angular.forEach(data.allMenus, formatSysMenuData);
			
			angular.forEach($scope.allPages, function(data){
				if($scope.rightMenusMap[data.id])
					data.selected=true;
				$scope.allPagesMap[data.id] = data;
			});
			
			$scope.sysMenus = data.allMenus;
			$scope.currMenu = data.allMenus[0];
			if(!$scope.currMenu.state)
				$scope.currMenu.state = {};
			$scope.currMenu.state.selected=true;
			flags[1] = true;
			$scope.refreshPermits();
			$scope.reCreateTree();
		});
	};
	$scope.changedCB = function(ev, data) {
		$scope.checkFlag = false;
		if (data.action != "select_node")
			return;
		$scope.$apply(function() {
		var item = data.node.original;
		$scope.currMenu = item;
		$scope.refreshPermits();
		});
	};
	
	$scope.refreshPermits = function(){
		$scope.showPages = [];
		angular.forEach($scope.allPages,function(data,index){
			if((data.menuType=='page' && data.parentId==$scope.currMenu.id) || data.id == $scope.currMenu.id){
				$scope.showPages.push(data);
			}
		});
	};
	
	$scope.savePermits = function(){
		$scope.submitting = true;
		if(!$scope.currMenu.id || !$scope.allPages)
			return;
		var param = {"rightId": $stateParams.id,"menuIds":[]};
		angular.forEach($scope.allPages, function(data){
			if(data.selected){
				param.menuIds.push(data.id);
			}
		});
//			param.menuId = param.menuId.join(',');
		$http.post("right/editRightMenu.do",angular.toJson(param))
			.success(function(msg){
				if(msg.status){
					$scope.notice(msg.msg);
					//返回上一级
					history.go(-1);
				} else {
					$scope.notice(msg.msg);
				}
				$scope.submitting = false;
			}).error(function(){
				$scope.submitting = false;
			});
	};
	
	$scope.refreshTree();
	
	$scope.checkFlag = false;
	//全选
	$scope.checkAll = function(){
		if($scope.checkFlag){
			angular.forEach($scope.showPages,function(data){
				data.selected = true;
			})
		} else {
			angular.forEach($scope.showPages,function(data){
				data.selected = false;
			})
		}
		
	}
	
	//返回
	$scope.cancel = function(){
		history.go(-1);
	}
	
});