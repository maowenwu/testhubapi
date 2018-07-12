/**
 * INSPINIA - Responsive Admin Theme
 *
 */
(function () {
    angular.module('inspinia', [
        'ui.router',                    // Routing
        'oc.lazyLoad',                  // ocLazyLoad
        'ui.bootstrap',                 // Ui Bootstrap
        'ngIdle',                       // Idle timer
        'ngSanitize',                  // ngSanitize
        'ui.grid',                      // ui.grid
        'ui.grid.pagination',          // ui.grid.pagination  分页
        'ui.grid.selection',           // ui.grid.selection   选择
        'ui.grid.pinning',
        'ui.grid.resizeColumns',
        'ui.grid.treeView',
        'cgNotify',
        'ui.grid.saveState',
        'ui.grid.exporter'
    ])
})();

