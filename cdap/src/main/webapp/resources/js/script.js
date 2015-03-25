
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

function findCoordinatesInitial(cardElementId,panelId) {
	findCoordinates(document.getElementById(cardElementId),panelId);
}

function findCoordinatesFront(cardElement) {
	findCoordinates(cardElement,'mainForm:frontSide');
}

function findCoordinatesBack(cardElement) {
	findCoordinates(cardElement,'mainForm:backSide');
}

function findCoordinates(cardElement,panelId) {
	selectElement(cardElement,panelId);
	card=document.getElementById(panelId);
	var cardRect = card.getBoundingClientRect();
	var elementRect = cardElement.getBoundingClientRect();
//	console.log(convertPixelsToMilimeters(cardElement.offsetTop-card.offsetTop),convertPixelsToMilimeters(cardElement.offsetLeft-card.offsetLeft));
	var x=convertPixelsToMilimeters(cardElement.offsetLeft-card.offsetLeft).toFixed(2);
	var y=convertPixelsToMilimeters(cardElement.offsetTop-card.offsetTop).toFixed(2);
	document.getElementById('mainForm:elementPositionX').value=x;
	document.getElementById('mainForm:elementPositionY').value=y;
	document.getElementById('mainForm:selectedId').value=cardElement.id;
	updateElementStyleAndPosition(cardElement,true);
	displayFormValues();
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

function selectElement(element,panelId) {
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
	if (setPositionOnly == false) {
		var elementName=document.getElementById('mainForm:elementName').value;
		var elementWidth=document.getElementById('mainForm:elementWidth').value;
		var elementHeight=document.getElementById('mainForm:elementHeight').value;
		var elementEditor=document.getElementById('mainForm:elementEditor_input').value; //editor unutar sebe ima input element, njega treba namjestiti
		callUpdater(cardElement.id,x,y,elementName,elementWidth,elementHeight,elementEditor);
	}
	else {
		callUpdater(cardElement.id,x,y,null,null,null,null);
	}
	
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

