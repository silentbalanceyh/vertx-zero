const colors = require("colors");
colors.setTheme({
    silly: 'rainbow',
    input: 'grey',
    verbose: 'cyan',
    prompt: 'red',
    info: 'green',
    data: 'blue',
    help: 'cyan',
    warn: 'yellow',
    debug: 'magenta',
    error: 'red'
});
module.exports = {
    info: (message) => console.log(`#[信息]`.green.bold + ` ${message}`),
    warn: (message) => console.log(`#[警告]`.green.bold + ` ${message}`.yellow),
    write: (message) => console.log(`#[IO]`.data.bold + ` ${message}`.debug),
    error: (message) => console.log(`#[错误]`.green.bold + ` ${message}`.red)
};