import * as esbuild from 'esbuild';
import path from 'path';
import { fileURLToPath } from 'url';
import 'dotenv/config';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

async function buildK6Script() {
    const env = process.env;
    const define = Object.entries(env)
        .filter(([key]) => key.startsWith('VITE_'))
        .reduce((acc, [key, value]) => {
            acc[`import.meta.env.${key}`] = JSON.stringify(value);
            acc[`__ENV.${key.replace('VITE_', '')}`] = JSON.stringify(value);
            return acc;
        }, {});

    try {
        await esbuild.build({
            entryPoints: [path.resolve(__dirname, 'src/k6/*.ts')],
            bundle: true,
            outdir: path.resolve(__dirname, 'dist'),
            format: 'esm',
            target: "es2016",
            platform: 'browser',
            sourcemap: true,
            minify: false,
            external: ["k6"],
            define,
            alias: {
                '~': path.resolve(__dirname, 'src'),
            }
        });
        console.log('esbuild: bundling k6 script done');
    } catch (error) {
        console.error('esbuild: bundling k6 script failed:', error);
        process.exit(1);
    }
}

buildK6Script();