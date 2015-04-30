
var disableEditorUpdate=false;
var currentlySelected;
var currentlySelectedPanel;

function markSelectedImage(image) {
	selectBackground([{name:'image', value:image}]);
}

function selectImageForUpload(cardTypeName,elementId) {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			showImageDialog();
		}
	};
	
	console.log(cardTypeName+', ' + elementId);
	xmlhttp.open("GET", "ImageSelector?cardTypeName=" + cardTypeName + "&elementId=" + elementId + "&random=" + Math.random(), true);
	xmlhttp.send();
}

function callUpdater(id, x, y, name, width, height, editor) {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {}
	};

	xmlhttp.open("GET", "Updater?id=" + id + "&x=" + x + "&y=" + y + "&elementName="+ name + "&elementWidth=" + width + "&elementHeight=" + height+ "&elementEditor=" + editor + "&random=" + Math.random(), true);
	xmlhttp.send();
}

function updateValueOnFormWithAjax(elementId, objectName, fieldName) {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {}
	};

	xmlhttp.open("GET", "Fetcher?elementId=" + elementId + "&objectName=" + objectName+ "&random=" + Math.random(), true);
	xmlhttp.send();
	
}

function updateEditorValueOnFormWithAjax(elementId, objectName) {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {}
	};

	xmlhttp.open("GET", "Fetcher?elementId=" + elementId + "&objectName=" + objectName+ "&random=" + Math.random(), true);
	xmlhttp.send();
	
}


function updateEditorValue(value) {
	PF('elementEditorWidgetVar').clear();
	PF('elementEditorWidgetVar').enable();
	if (value != 'null') {
		PF('elementEditorWidgetVar').editor.focus();
		setTimeout(function() {
			PF('elementEditorWidgetVar').editor.execCommand('inserthtml', value, false);
		}, 0);
		return false;
	}
}

function updateValueOnFormAndObjectWithAjax (elementId, objectName, fieldName,newValue) {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			document.getElementById(fieldName).value=xmlhttp.responseText;
			console.log('Postavljena vrijednost za ' + fieldName+': ' + newValue);
		}
	};

	xmlhttp.open("GET", "Fetcher?elementId=" + elementId + "&objectName=" + objectName+"&newValue=" + newValue+ "&random=" + Math.random(), true);
	xmlhttp.send();
	
}


function updateValueOnObjectWithAjax (elementId, objectName, newValue) {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {}
	};

	xmlhttp.open("GET", "Fetcher?elementId=" + elementId + "&objectName=" + objectName+"&newValue=" + newValue+ "&random=" + Math.random(), true);
	xmlhttp.send();
	
}


function selectElementInitial(cardElementId,panelId) {
	selectElement(document.getElementById(cardElementId),panelId);
}

function selectElementFront(cardElement) {
	selectElement(cardElement,'mainForm:frontSide');
}

function selectElementBack(cardElement) {
	selectElement(cardElement,'mainForm:backSide');
}


function selectElement(cardElement,panelId) {
	
	var x1= convertPixelsToMilimeters(document.getElementById('mainForm:cardElement0').offsetLeft-document.getElementById(panelId).offsetLeft).toFixed(2);
	var y1= convertPixelsToMilimeters(document.getElementById('mainForm:cardElement0').offsetTop-document.getElementById(panelId).offsetTop).toFixed(2);
	
	var x2=  convertPixelsToMilimeters(document.getElementById('cardElement0').offsetLeft-document.getElementById(panelId).offsetLeft).toFixed(2);
	var y2= convertPixelsToMilimeters(document.getElementById('cardElement0').offsetTop-document.getElementById(panelId).offsetTop).toFixed(2);
	
	
	console.log('test 1X: ' + x1);
	console.log('test 1X: ' + y1);
	
	console.log('test 2X: ' + x2);
	console.log('test 2X: ' + y2);
	
	
	
	document.getElementById(panelId).appendChild(document.getElementById('cardElement0')); 
	
	var cardElementId=cardElement.id;
	
	var startWithMainForm=cardElementId.startsWith('mainForm');
	if (startWithMainForm == true) {
		cardElementIdForCalculation=cardElement.id.substring(9, cardElement.id.length);
		cardElement=document.getElementById(cardElementIdForCalculation);
	}
	
	
	
	if (startWithMainForm == false) {
		cardElementId='mainForm:'+cardElement.id;
	}
	
	document.getElementById('mainForm:selectedId').value=cardElementId;
	
	console.log('Card element: ' + cardElement.id);
	console.log('Panel: ' + panelId);
	console.log('ID: ' + cardElementId);
	
	markElementSelected	(cardElement,panelId);
	card=document.getElementById(panelId);
	var cardRect = card.getBoundingClientRect();
	var elementRect = cardElement.getBoundingClientRect();
	
	
	//ovo se izracuna
	var x=convertPixelsToMilimeters(cardElement.offsetLeft-card.offsetLeft).toFixed(2);
	var y=convertPixelsToMilimeters(cardElement.offsetTop-card.offsetTop).toFixed(2);
	
	
	//var x=x2+x1;
	//var y=y2+y1;
	
	
	//korektivni faktor zbog headera
	y=y-12;
	
	disableEditorUpdate=true;
	console.log('izracunate kooridnate za ' + cardElement.id+': ' + x + ' - ' + y + ', disableEditorUpdate: ' + disableEditorUpdate);
	
	//vrijednosti na formi se popunjavaju sa ajaxom
	
	
	
	
	console.log('update start 1... ' + cardElementId);
	updateValueOnObjectWithAjax(cardElementId,'elementX',x);
	updateValueOnObjectWithAjax(cardElementId,'elementY',y);
	console.log('update finished 1... ' + cardElementId);
	
	
	//refreshAdditionalForm();
	
	
}


function clearForm() {
	disableEditorUpdate=true;
	document.getElementById('mainForm:selectedId').value='';
	document.getElementById('mainForm:elementPositionX').value='';
	document.getElementById('mainForm:elementPositionY').value='';
	document.getElementById('mainForm:elementName').value='';
	document.getElementById('mainForm:elementWidth').value='';
	document.getElementById('mainForm:elementHeight').value='';
	document.getElementById('mainForm:elementDataType').value='';
	document.getElementById('mainForm:elementMinimumLength').value='';
	document.getElementById('mainForm:elementMaximumLength').value='';
	document.getElementById('mainForm:dateFormat').value='';
	document.getElementById('mainForm:elementRequired').value='';
	PF('elementEditorWidgetVar').clear();
}



function convertPixelsToMilimeters(pixels) {
	return pixels*0.264583333;
}

function convertMilimetersToPixels(milimeters) {
	return (milimeters/0.264583333).toFixed(0);
}

function moveToPosition() {
	panel=document.getElementById(currentlySelectedPanel);
	var panelRect = panel.getBoundingClientRect();
	var pxTop=parseInt(convertMilimetersToPixels(document.getElementById('mainForm:elementPositionY').value));
	var pxLeft=parseInt(convertMilimetersToPixels(document.getElementById('mainForm:elementPositionX').value));
	var startTop=panelRect.top;
	var startLeft=panelRect.left;
	var newTop=pxTop+startTop;
	var newLeft=pxLeft+startLeft;
	
	currentlySelected.style.position = "absolute";
	currentlySelected.style.left = newLeft+"px";
	currentlySelected.style.top=newTop+"px";
	updateElementStyleAndPosition(currentlySelected,false);
}

function moveToPositionInitial(selectedPanel,selectedElement,x,y) {
	panel=document.getElementById(selectedPanel);
	var panelRect = panel.getBoundingClientRect();
	var pxTop=parseInt(convertMilimetersToPixels(y));
	var pxLeft=parseInt(convertMilimetersToPixels(x));
	var startTop=panelRect.top;
	var startLeft=panelRect.left;
	var newTop=pxTop+startTop;
	var newLeft=pxLeft+startLeft;
	initialSelectedElement=document.getElementById(selectedElement);
	initialSelectedElement.style.position = "absolute";
	initialSelectedElement.style.left = newLeft+"px";
	initialSelectedElement.style.top=newTop+"px";
	
	console.log('nove koordinate za '+selectedElement+'... ' + newLeft + ', ' + newTop + ', ' + x + ', ' + y);
	
}

function updateElementMap() {
	console.log('update element map... ' + currentlySelected.id);
	updateElementStyleAndPosition(currentlySelected,false);
	redrawElementByFormValues();
}

function updateElementEditorMap() {
	var type=document.getElementById('mainForm:elementType').value;
	console.log('update element editor map... ' + currentlySelected.id + ', ' + type + ', ' + disableEditorUpdate);
	if (disableEditorUpdate == false && type != '3' && type != '4') {
			var elementEditor=document.getElementById('mainForm:elementEditor_input').value; //editor unutar sebe ima input element, njega treba namjestiti
			callUpdater(currentlySelected.id,null,null,null,null,null,elementEditor);
			currentlySelected.innerHTML=elementEditor;
	}
	disableEditorUpdate=false;
}



function redrawElementByFormValues() {
	
	panel=document.getElementById(currentlySelectedPanel);
	var panelRect = panel.getBoundingClientRect();
	var pxTop=parseInt(convertMilimetersToPixels(document.getElementById('mainForm:elementPositionY').value));
	var pxLeft=parseInt(convertMilimetersToPixels(document.getElementById('mainForm:elementPositionX').value));
	var pxWidth=parseInt(convertMilimetersToPixels(document.getElementById('mainForm:elementWidth').value));
	var pxHeight=parseInt(convertMilimetersToPixels(document.getElementById('mainForm:elementHeight').value));
	var elementEditor=document.getElementById('mainForm:elementEditor_input').value;
	
	var startTop=panelRect.top;
	var startLeft=panelRect.left;
	var newTop=pxTop+startTop;
	var newLeft=pxLeft+startLeft;
	
	currentlySelected.style.position = "absolute";
	currentlySelected.style.left = newLeft+"px";
	currentlySelected.style.top=newTop+"px";
	currentlySelected.style.width = pxWidth+"px";
	currentlySelected.style.height = pxHeight+"px";
}

function markElementSelected(element,panelId) {
	currentlySelectedPanel=panelId;
	if (currentlySelected != null) {
		var oldStyle=currentlySelected.getAttribute("style");
		oldStyle=oldStyle+'border:0px;';
		currentlySelected.setAttribute("style", oldStyle);
	}
	var style=element.getAttribute("style");
	style=style+'border:1px solid red;';
	element.setAttribute("style", style);
	currentlySelected=element;
}

function updateElementStyleAndPosition(cardElement,setPositionOnly) {
	
	var x=document.getElementById('mainForm:elementPositionX').value;
	var y=document.getElementById('mainForm:elementPositionY').value;
	var elementName=document.getElementById('mainForm:elementName').value;
	var elementWidth=document.getElementById('mainForm:elementWidth').value;
	var elementHeight=document.getElementById('mainForm:elementHeight').value;
	callUpdater(cardElement.id,x,y,elementName,elementWidth,elementHeight,null);
}

function updateImageSize(imagePanel,imageElement) {
	var x=document.getElementById('mainForm:elementPositionX').value;
	var y=document.getElementById('mainForm:elementPositionY').value;
	var elementName=document.getElementById('mainForm:elementName').value;
	var elementWidth=convertPixelsToMilimeters(imageElement.width).toFixed(2);
	var elementHeight=convertPixelsToMilimeters(imageElement.height).toFixed(2);
	document.getElementById('mainForm:elementWidth').value=elementWidth;
	document.getElementById('mainForm:elementHeight').value=elementHeight;
	callUpdater(imagePanel.id,x,y,elementName,elementWidth,elementHeight,null);
}

function resizeImage(imagePanelId) {
	cardElement=document.getElementById(imagePanelId+'_image');
	imagePanel=document.getElementById(imagePanelId);
	updateImageSize(imagePanel,cardElement);
}

