var pos = context.properties["pos"];
var size = context.properties["size"];

// get a connector to the Alfresco repository endpoint
var connector = remote.connect("alfresco");

// retrieve the web script index page
var auditEntryListJSON = connector.get("/enkive/audit/recent?pos=" + pos
		+ "&size=" + size);
var auditEntryList = eval("(" + auditEntryListJSON + ")");
model.result = auditEntryList

model.paging = auditEntryList.paging
model.uri = url.uri + "?";