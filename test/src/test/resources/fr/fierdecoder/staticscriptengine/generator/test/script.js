function voidMethod() {
}

function voidMethodWithParameters(param1, param2) {
    if (param1 != 'a' || param2 != 2) {
        throw new Exception();
    }
}

function myFunction(param1) {
    return 'hello ' + param1;
}
