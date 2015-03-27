
var currentlySelected;
var currentlySelectedPanel;


function callUpdater(id, x, y, name, width, height, editor) {
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {}
	}

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
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			if (xmlhttp.responseText != 'null') {
				document.getElementById(fieldName).value=xmlhttp.responseText;
			}
			else {
				document.getElementById(fieldName).value='';
			}
		}
	}

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
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			if (xmlhttp.responseText != 'null') {
				updateEditorValue(xmlhttp.responseText);
			}
		}
	}

	xmlhttp.open("GET", "Fetcher?elementId=" + elementId + "&objectName=" + objectName+ "&random=" + Math.random(), true);
	xmlhttp.send();
	
}


function updateEditorValue(value) {
	PF('elementEditorWidgetVar').clear();
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
	}

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
	markElementSelected	(cardElement,panelId);
	card=document.getElementById(panelId);
	var cardRect = card.getBoundingClientRect();
	var elementRect = cardElement.getBoundingClientRect();
//	console.log(convertPixelsToMilimeters(cardElement.offsetTop-card.offsetTop),convertPixelsToMilimeters(cardElement.offsetLeft-card.offsetLeft));
	
	//ovo se izracuna
	var x=convertPixelsToMilimeters(cardElement.offsetLeft-card.offsetLeft).toFixed(2);
	var y=convertPixelsToMilimeters(cardElement.offsetTop-card.offsetTop).toFixed(2);
	//document.getElementById('mainForm:elementPositionX').value=x;
	//document.getElementById('mainForm:elementPositionY').value=y;
	//document.getElementById('mainForm:selectedId').value=cardElement.id;
	
	
	//vrijednosti na formi se popunjavaju sa ajaxom
	updateValueOnFormWithAjax(cardElement.id,'elementName','mainForm:elementName');
	updateValueOnFormWithAjax(cardElement.id,'elementId','mainForm:selectedId');
	updateValueOnFormWithAjax(cardElement.id,'elementWidth','mainForm:elementWidth');
	updateValueOnFormWithAjax(cardElement.id,'elementHeight','mainForm:elementHeight');
	
	updateEditorValueOnFormWithAjax(cardElement.id,'elementEditor'); //netreba id elementa jer ide prek widget var-a
	
	updateValueOnFormAndObjectWithAjax(cardElement.id,'elementX','mainForm:elementPositionX',x);
	updateValueOnFormAndObjectWithAjax(cardElement.id,'elementY','mainForm:elementPositionY',y);
	
	
	updateValueOnFormWithAjax(cardElement.id,'elementDataType','mainForm:elementDataType');
	updateValueOnFormWithAjax(cardElement.id,'elementMinChar','mainForm:elementMaximumLength');
	updateValueOnFormWithAjax(cardElement.id,'elementMaxChar','mainForm:elementMinimumLength');
	updateValueOnFormWithAjax(cardElement.id,'elementDateFormat','mainForm:dateFormat');
	updateValueOnFormWithAjax(cardElement.id,'elementCardNumber','mainForm:startCardNumber');
	updateValueOnFormWithAjax(cardElement.id,'elementRequired','mainForm:elementRequired');
	
	/*
	var width=convertPixelsToMilimeters(cardElement.offsetLeft-card.offsetLeft).toFixed(2);
	var height=convertPixelsToMilimeters(cardElement.offsetTop-card.offsetTop).toFixed(2);
	
	
	document.getElementById('mainForm:elementPositionY').value;
	document.getElementById('mainForm:elementName').value;
	document.getElementById('mainForm:elementWidth').value;
	document.getElementById('mainForm:elementHeight').value;
	document.getElementById('mainForm:elementEditor_input').value; //editor unutar sebe ima input element, njega treba namjestiti
	*/
	
	
	//updateElementStyleAndPosition(cardElement);
	//displayFormValues();
	
	console.log('Nova pozicija za ' +cardElement.id+ ': ' +  x+', ' + y);
	
	
}


function clearForm() {
	document.getElementById('mainForm:selectedId').value='';
	document.getElementById('mainForm:elementPositionX').value='';
	document.getElementById('mainForm:elementPositionY').value='';
	document.getElementById('mainForm:elementName').value='';
	document.getElementById('mainForm:elementWidth').value='';
	document.getElementById('mainForm:elementHeight').value='';
	document.getElementById('mainForm:elementEditor_input').value=''; 
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
//	console.log(panelRect.top, panelRect.right, panelRect.bottom, panelRect.left);
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
	initialSelectedElement=document.getElementById(selectedElement)
	initialSelectedElement.style.position = "absolute";
	initialSelectedElement.style.left = newLeft+"px";
	initialSelectedElement.style.top=newTop+"px";
	
}

function updateElementMap() {
	updateElementStyleAndPosition(currentlySelected,false);
	saveFormValues();
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
	var elementEditor=document.getElementById('mainForm:elementEditor_input').value; //editor unutar sebe ima input element, njega treba namjestiti
	callUpdater(cardElement.id,x,y,elementName,elementWidth,elementHeight,elementEditor);
	
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
	saveImageSize();
}



