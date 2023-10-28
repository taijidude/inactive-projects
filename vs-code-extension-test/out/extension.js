"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const vscode = require("vscode");
class SelectionData {
    constructor(editor) {
        this.editor = editor;
        this.doc = editor === null || editor === void 0 ? void 0 : editor.document;
        this.allSelections = editor.selections;
        this.currentSelection = this.allSelections[0];
        this.text = this.doc.getText(this.currentSelection);
    }
    setSelection(index) {
        this.currentSelection = this.allSelections[index];
        this.text = this.doc.getText(this.currentSelection);
    }
}
function getSelectionData(editor) {
    if (editor) {
        return new SelectionData(editor);
    }
    else {
        throw new Error('No active Editor!');
    }
}
// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
function activate(context) {
    console.log('Congratulations, your extension "text-utils" is now active!');
    context.subscriptions.push(vscode.commands.registerCommand('extension.helloWorld', () => {
        vscode.window.showInformationMessage('Hello World!');
    }));
    context.subscriptions.push(vscode.commands.registerCommand('extension.lollauch', () => {
        var _a;
        let sd = getSelectionData(vscode.window.activeTextEditor);
        console.log(sd.text);
        sd.text = 'Haha Trottel';
        (_a = sd.editor) === null || _a === void 0 ? void 0 : _a.edit(editBuilder => {
            editBuilder.replace(sd.currentSelection, sd.text);
        });
    }));
    //flip Slashes
    context.subscriptions.push(vscode.commands.registerCommand('extension.flipslash', () => {
        var _a, _b;
        let sd = getSelectionData(vscode.window.activeTextEditor);
        console.log(sd.text);
        let re_slash = new RegExp('/', 'g');
        let re_backslash = new RegExp('\\\\', 'g');
        if (re_slash.test(sd.text)) {
            sd.text = sd.text.replace(re_slash, "\\");
            (_a = sd.editor) === null || _a === void 0 ? void 0 : _a.edit(editBuilder => {
                editBuilder.replace(sd.currentSelection, sd.text);
            });
        }
        else if (re_backslash.test(sd.text)) {
            sd.text = sd.text.replace(re_backslash, "/");
            (_b = sd.editor) === null || _b === void 0 ? void 0 : _b.edit(editBuilder => {
                editBuilder.replace(sd.currentSelection, sd.text);
            });
        }
    }));
    //Escape Backslash
    context.subscriptions.push(vscode.commands.registerCommand('extension.flipslash', () => {
        var _a;
        let sd = getSelectionData(vscode.window.activeTextEditor);
        console.log(sd.text);
        let re_slash = new RegExp('/', 'g');
        let re_backslash = new RegExp('\\\\', 'g');
        if (re_slash.test(sd.text)) {
            sd.text = sd.text.replace(re_slash, "\\\\");
            (_a = sd.editor) === null || _a === void 0 ? void 0 : _a.edit(editBuilder => {
                editBuilder.replace(sd.currentSelection, sd.text);
            });
        }
    }));
}
exports.activate = activate;
// this method is called when your extension is deactivated
function deactivate() { }
exports.deactivate = deactivate;
//# sourceMappingURL=extension.js.map