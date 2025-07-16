import mysql2 from "mysql2";
import { MongoClient } from "mongodb";
import { Elysia } from 'elysia'
import { node } from '@elysiajs/node'

import "dotenv/config";

const mysql = mysql2.createPool({
    host: process.env.DB_HOST || "localhost",
    port: Number.parseInt(process.env.DB_PORT || "3306"),
    database: process.env.DB_NAME,
    user: process.env.DB_USERNAME,
    password: process.env.DB_PASSWORD,
})

const mongo = new MongoClient(process.env.MONGODB_URI || "mongodb://localhost:27017", {
    auth: {
        username: process.env.MONGODB_USERNAME,
        password: process.env.MONGODB_PASSWORD
    }
});
const collection = mongo
  .db(process.env.MONGODB_DATABASE || "roadeye")
  .collection(process.env.MONGODB_COLLECTION || "vus");

const store = new Elysia().state({})
const app = new Elysia({
    adapter: node()
})
.use(store)
.get("/cars/:id", ({ params }) => {
    return new Promise((resolve, reject) => {
        mysql.query(`SELECT id, active_tuid, mileage_sum FROM car WHERE id = ?`, [params.id], (err, results) => {
            if (err) {
                reject({
                    status: 500,
                    body: {
                        status: "error",
                        message: err.message
                    }
                });
            }
            else {
                resolve({
                    id: results[0].id,
                    active_tuid: results[0].active_tuid,
                    mileage_sum: results[0].mileage_sum
                });
            }
        });
    });
})
.get("/vus/:id", async ({ params }) => {
    const vu = parseInt(params.id);
    return await collection.findOne({ vu })
})
.put("/vus/:id", async ({ params, body }) => {
    const vu = parseInt(params.id);
    return await collection.updateOne({ vu }, { $set: { vu, body } }, { upsert: true })
})
.delete("/vus/:id", async ({ params }) => {
    const vu = parseInt(params.id);
    return await collection.deleteOne({ vu })
})
.onError(({ error }) => {
    console.error(error);
    return {
        status: 500,
        body: { status: "error" }
    }
})
.listen(process.env.PORT || "5678");

console.log(`Server is running ${app.config}`)