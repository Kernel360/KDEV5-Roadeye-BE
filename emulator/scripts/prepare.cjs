const fs = require('fs');
const path = require('path');
const uuid = require('uuid');
const { exec, fork } = require('node:child_process');

(() => {
    const data = {
        "dateformat": "https://cdn.jsdelivr.net/npm/dateformat@5.0.3/lib/dateformat.min.js",
    }

    Object
        .entries(data)
        .forEach(([name, url]) => fetch(url)
            .then(res => res.text())
            .then(text => ({ name, text }))
            .then(({ name, text }) => {
                const outPath = `./src/lib/vendor/${name}.js`;
                fs.writeFileSync(outPath, text);
            })
            .catch(err => {
                console.error(err);
            })
        );
})();

(() => {
    const bin = path.normalize('./node_modules/.bin/browserify');
    const moduleNames = {
        'geolocation-utils': 'geolocation-utils',
        'uuid': 'uuid/index.js'
    }

    Object.entries(moduleNames)
        .map(([name, libPath]) => ({
            name,
            outPath: path.normalize(`./src/lib/vendor/${name}.js`),
            libPath
        }))
        .forEach(({ name, outPath, libPath }) => {
            const modulePath = path.normalize(`./node_modules/${libPath}`);
            const cmd = `${bin} ${modulePath} -s ${name} -o ${outPath}`;
            exec(cmd, (
                err, stdout, stderr
            ) => {
                if (err) {
                    console.error(err);
                }
            });
        });
})();

(() => {
    const data = JSON.parse(fs.readFileSync('./src/data/cars.json', 'utf8'));
    const cars = data.map(car => {
        if (car.activeTransactionId) {
            return car;
        }

        const transactionId = uuid.v4();
        return {
            ...car,
            activeTransactionId: transactionId
        }
    });

    fs.writeFileSync('./src/data/cars.json', JSON.stringify(cars, null, 2));
    fs.writeFileSync('./src/data/car.example.json', JSON.stringify(cars[0], null, 2));
})();
