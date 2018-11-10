const fields = require('express').Router();
const Field = require('../models').Field;
const Location = require('../models').Location;
const Sequelize = require('../models').Sequelize;

// Custom Response Handler
const { sendCustomResponse, sendCustomErrorResponse } = require('../handlers/customResponse');

// List fields
fields.get('/', async (req, res) => {
    try {
        const fields = await Field.findAll({
            include: [{
                model: Location,
                attributes: {
                    exclude: ['id', 'createdAt', 'updatedAt']
                }
            }],
            attributes: {
                exclude: ['locationId', 'createdAt', 'updatedAt']
            }
        });

        if (fields !== null) {
            // Rename Location attribute to location
            fields.map((field) => {
                field.dataValues.location = field.Location;
                delete field.dataValues.Location;
            });

            sendCustomResponse(res, 200, fields);
        } else {
            // Send empty array of fields
            sendCustomResponse(res, 200, []);
        }
    } catch (error) {
        // TODO: Log error
        console.log(error);
        sendCustomErrorResponse(res, 500, "An error occurred while listing fields.");
    }
});

module.exports = fields;