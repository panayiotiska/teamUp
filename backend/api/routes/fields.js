const fields = require('express').Router();
const Field = require('../models').Field;
const Location = require('../models').Location;
const fieldRating = require('../models').fieldRating;
const fieldAvgRatings = require('../models').fieldAvgRatings;
const User = require('../models').User;
const Sequelize = require('../models').Sequelize;

// Custom Response Handler
const { sendCustomResponse, sendCustomErrorResponse } = require('../handlers/customResponse');

// Create field
fields.post('/', async (req, res) => {
    try {
        // Find the user(id) that wants to create this field
        // TODO: Use authorization and authentication to find out who the user really is.
        const user = await User.findOne({
            where: {
                authToken: req.headers["auth-token"]
            }
        });

        if (user !== null) {
            // Create location
            await Location.findOrCreate({
                where: {
                    city: req.body.location.city,
                    address: req.body.location.address,
                    countryCode: req.body.location.countryCode
                }, defaults: {
                    city: req.body.location.city,
                    address: req.body.location.address,
                    country: req.body.location.country,
                    countryCode: req.body.location.countryCode,
                    postalCode: req.body.location.postalCode,
                    latitude: req.body.location.latitude,
                    longitude: req.body.location.longitude
                }
            }).spread(async (location, created) => {
                // Location created successfully
                if (location !== null) {
                    // Build Game instance
                    const field = await Field.create({
                        name: req.body.name,
                        type: req.body.type,
                        createdBy: user.id,
                        contactPhone: req.body.contactPhone,
                        imageUrl: req.body.imageUrl,
                        sponsored: false,
                        verified: false,
                        locationId: location.id
                    });
                    // Guard
                    // Validate data against Field model
                    await field.validate();

                    // Save
                    await field.save();

                    // Create default average ratings
                    const avgRating = await fieldAvgRatings.create({
                        totalAvg: 0.0,
                        ratingsCount: 0
                    }); 

                    // Set field default rating values to 0 (zero)
                    await avgRating.setField(field);

                    // Send response - HTTP 201 Created
                    sendCustomResponse(res, 201, [field]);
                } else {
                    sendCustomErrorResponse(res, 500, "Couldn't create location.");
                }
            });
        } else {
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action.");
        }
    } catch (error) {
        //TODO: Log error
        console.log(error);
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "An error occurred while creating field.");
    }
});

// List fields
fields.get('/', async (req, res) => {
    try {
        const fields = await Field.findAll({
            where: {
                verified: true
            },
            include: [{
                model: Location,
                attributes: {
                    exclude: ['id', 'createdAt', 'updatedAt']
                }
            },
            {
                model: fieldAvgRatings,
                attributes: {
                    exclude: ['id', 'createdAt', 'updatedAt', 'FieldId']
                }
            }],
            attributes: {
                exclude: ['locationId', 'createdBy', 'createdAt', 'updatedAt']
            }
        });

        if (fields !== null) {
            fields.map((field) => {
                // Rename Location attribute to location
                field.dataValues.rating = field.fieldAvgRating;
                delete field.dataValues.fieldAvgRating;

                // Rename Location attribute to location
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

// Get field
fields.get('/:id', async (req, res) => {
    try {
        const field = await Field.findOne({
            where: {
                id: req.params.id
            },
            include: [{
                model: Location,
                attributes: {
                    exclude: ['id', 'createdAt', 'updatedAt']
                }
            },
            {
                model: fieldAvgRatings,
                attributes: {
                    exclude: ['id', 'createdAt', 'updatedAt', 'FieldId']
                }
            }],
            attributes: {
                exclude: ['locationId', 'createdBy', 'createdAt', 'updatedAt']
            }
        });

        if (field !== null) {
            // Rename fieldAvgRating to rating
            field.dataValues.rating = field.fieldAvgRating;
            delete field.dataValues.fieldAvgRating;

            // Rename Location attribute to location
            field.dataValues.location = field.Location;
            delete field.dataValues.Location;

            sendCustomResponse(res, 200, [field]);
        } else {
            // Send empty array of field
            sendCustomResponse(res, 200, []);
        }
    } catch (error) {
        // TODO: Log error
        console.log(error);
        sendCustomErrorResponse(res, 500, "An error occurred while getting field.");
    }
});

// List field ratings
fields.get('/:id/ratings', async (req, res) => {
    try {
        const field = await Field.findOne({
            where: {
                id: req.params.id
            },
            include: [{
                model: fieldRating,
                through: 'fieldRatingsData',
                through: {
                    attributes: []
                }

            }],
            attributes: []
        });

        if (field !== null) {
            sendCustomResponse(res, 200, field.fieldRatings)
        }
    } catch (error) {
        console.log(error);
        sendCustomErrorResponse(res, 500, "An error occurred while listing field ratings.");
    }
});

module.exports = fields;