import * as vscode from 'vscode';

class SelectionData {
	editor:vscode.TextEditor;
	doc:vscode.TextDocument;
	allSelections: vscode.Selection[]; 
	currentSelection: vscode.Selection ;
	text: string;

	constructor(editor: vscode.TextEditor ) {
		this.editor = editor;
		this.doc = editor?.document;
		this.allSelections = editor.selections;
		this.currentSelection = this.allSelections[0];
		this.text = this.doc.getText(this.currentSelection);
	}

	setSelection(index: number) {
		this.currentSelection = this.allSelections[index];
		this.text = this.doc.getText(this.currentSelection);
	}
}

function getSelectionData(editor?: vscode.TextEditor) {
	if(editor) {
		return new SelectionData(editor);
	} else {
		throw new Error('No active Editor!');
	}
}

// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
	console.log('Congratulations, your extension "text-utils" is now active!');
	context.subscriptions.push(vscode.commands.registerCommand('extension.helloWorld', () => {
			vscode.window.showInformationMessage('Hello World!');
	}));

	context.subscriptions.push(vscode.commands.registerCommand('extension.lollauch', () => {
		let sd = getSelectionData(vscode.window.activeTextEditor);
		console.log(sd.text);
		sd.text = 'Haha Trottel';
		sd.editor?.edit(editBuilder => {
			editBuilder.replace(sd.currentSelection, sd.text);
		});
	}));

	//flip Slashes
	context.subscriptions.push(vscode.commands.registerCommand('extension.flipslash', () => {
		let sd = getSelectionData(vscode.window.activeTextEditor);
		console.log(sd.text);
		let re_slash = new RegExp('/', 'g');
		let re_backslash = new RegExp('\\\\', 'g');
		if(re_slash.test(sd.text)) {
			sd.text = sd.text.replace(re_slash, "\\");
			sd.editor?.edit(editBuilder => {
				editBuilder.replace(sd.currentSelection, sd.text);
			});
		} else if(re_backslash.test(sd.text)) {
			sd.text = sd.text.replace(re_backslash, "/");
			sd.editor?.edit(editBuilder => {
				editBuilder.replace(sd.currentSelection, sd.text);
			});
		}
	}));

	//Escape Backslash
	context.subscriptions.push(vscode.commands.registerCommand('extension.flipslash', () => {
		let sd = getSelectionData(vscode.window.activeTextEditor);
		console.log(sd.text);
		let re_slash = new RegExp('/', 'g');
		let re_backslash = new RegExp('\\\\', 'g');
		if(re_slash.test(sd.text)) {
			sd.text = sd.text.replace(re_slash, "\\\\");
			sd.editor?.edit(editBuilder => {
				editBuilder.replace(sd.currentSelection, sd.text);
			});
		}
	}));


	
	

}
// this method is called when your extension is deactivated
export function deactivate() {}
