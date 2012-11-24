/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
function $category_addMethods(pa, categoryName) {

	pa.add = function(params) {
		var act = $Actions[categoryName + "_edit"];
		act.selector = pa.selector;
		var branch = pa.currentBranch;
		act(("category_parentId=" + (branch ? branch.getId() : ""))
				.addParameter(params));
	};

	pa.edit = function(params) {
		var act = $Actions[categoryName + "_edit"];
		act.selector = pa.selector;
		act(("category_id=" + pa.currentBranch.getId()).addParameter(params));
	};

	pa.del = function(params) {
		var act = $Actions[categoryName + "_delete"];
		act.selector = pa.selector;
		act(("category_id=" + pa.currentBranch.getId()).addParameter(params));
	};

	pa.move = function(up, last) {
		var branch = pa.currentBranch;
		var act = $Actions[categoryName + "_move"];
		act.selector = pa.selector;
		act(last ? $tree_move2(branch, up) : $tree_move(branch, up));
	};

	pa.expand = function() {
		pa.currentBranch.expand();
	};

	pa.collapse = function() {
		pa.currentBranch.collapse();
	};
}
