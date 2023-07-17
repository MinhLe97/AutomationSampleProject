const { defineConfig } = require("cypress");
const { addContext } = require("mochawesome/addContext");

module.exports = defineConfig({
  screenshotsFolder: "cypress/screenshots",
  reporter: "mochawesome",
  reporterOptions: {
    overwrite: false,
    html: false,
    json: true
  },
  e2e: {
    setupNodeEvents(on, config) {
      on('after:spec', (results) => {
        //
      })
    },
  },
  experimentalWebKitSupport: true
});
