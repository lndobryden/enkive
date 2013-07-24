function delete_recent_searches() {
	var ids = "";
	$(".idcheckbox:checkbox:checked").each(function() {
		ids = ids + "," + $(this).val();
	});
	$.get("/ediscovery/search/delete?searchids=" + ids, function(data) {
		$('#main').load('/ediscovery/search/recent/main',
				function(response, status, xhr) {
					if (xhr.status == 403) {
						location.reload();
					}
				});
	});
}

function save_recent_search(id) {
	$.get("/ediscovery/search/save?searchids=" + id, function(data) {
		window.location.replace("/ediscovery/search/saved");
	});
}

function update_search(id) {
	$.get("/ediscovery/search/update?searchids=" + id, function(data) {
		if (window.location.pathname.indexOf('saved') != -1 ||
			window.location.pathname.indexOf('recent') != -1) {
			window.location.reload();
		} else {
			var uri = "/ediscovery/search/recent/view?searchid=" + id + " #main";
			$('#main').load(uri,
				function(response, status, xhr) {
					if (xhr.status == 403) {
						location.reload();
					}
				});
		}
	});
}

function save_recent_searches() {
	var ids = "";
	$(".idcheckbox:checkbox:checked").each(function() {
		ids = ids + "," + $(this).val();
	});
	$.get("/ediscovery/search/save?searchids=" + ids, function(data) {
		window.location.replace("/ediscovery/search/saved");
	});
}


function delete_saved_searches() {
	var ids = "";
	$(".idcheckbox:checkbox:checked").each(function() {
		ids = ids + "," + $(this).val();
	});
	$.get("/ediscovery/search/delete?searchids=" + ids, function(data) {
		$('#main').load('/ediscovery/search/saved/main',
				function(response, status, xhr) {
					if (xhr.status == 403) {
						location.reload();
					}
				});
	});
}

function stop_search(id) {
	$
			.get(
					"/ediscovery/search/cancel?searchid=" + id,
					function(data) {
						$('#main')
								.load(
										'/ediscovery/search/recent/main',
										function(response, status, xhr) {
											if (xhr.status == 403) {
												location.reload();
											} else {
												$('.search_result')
														.click(
																function() {
																	var id = $(
																			this)
																			.attr(
																					"id");
																	if (id) {
																		window.location = "/ediscovery/search/recent/view?searchid="
																				+ id;
																	}
																});
											}
										});
					});
}
