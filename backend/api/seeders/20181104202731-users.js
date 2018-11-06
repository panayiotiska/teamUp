'use strict';

module.exports = {
  up: (queryInterface, Sequelize) => {
    /*
      Add altering commands here.
      Return a promise to correctly handle asynchronicity.
    */
      Example:
      return queryInterface.bulkInsert('Users', [
        {
          id: 100000273909010,
          firstName: "Rafaellos",
          lastName: "Monoyios",
          phoneNumber: "+306949873324",
          deviceToken: "078204242018045720D636907952AC02C903BFDB919BAB80EA429EA5A9C00D7D",
          authToken: "rafAuthToken"
        },
        {
          id: 100000273909020,
          firstName: "Panayiotis",
          lastName: "Kattides",
          phoneNumber: "+306976429248",
          deviceToken: "FDF06EB30E40C87E50FA7AB067BEB3364F458D4013CEB6504CF5D3B4EDFE0024",
          authToken: "panosAuthToken"
        },
        {
          id: 100000273909030,
          firstName: "Aldi",
          lastName: "Malka",
          phoneNumber: "+306983146727",
          deviceToken: "20DDE3B8ABC3643C9994664CFE66F7CEE969013B64FE5D9E1EE625A2D4C8D10E",
          authToken: "aldiAuthToken"
        },
        {id:100000273909099,firstName:"Oralla",lastName:"Daughtery",deviceToken:"3ee793699a88396482d96f7d4fac471d0af869d8d2193910c4088bd8ebde4e63",authToken:"c1f7d190-4f69-4263-a41f-a1ed36ce36cb"},
        {id:100000273909189,firstName:"Jessamyn",lastName:"Grestie",deviceToken:"7c74c5eb9166745808c41398c2e7573a89beb33659371277e3cc98be0aeef125",authToken:"1fd08d03-dbac-47c9-b678-be5a89ee2084"},
        {id:100000273909203,firstName:"Danita",lastName:"Boult",deviceToken:"b88b19236032186564605799f6e317205884aa6c9d3a31a4edb5a29c41724334",authToken:"3a3fb77a-a8b1-4eb3-bf21-cdf1213cfd4d"},
        {id:100000273909151,firstName:"Philis",lastName:"MacFadden",deviceToken:"733646e63f35f657ddfd82542c1f9f2aebc67f381bb4c7136f10e2a07ea2b031",authToken:"5b019fa0-bd18-4a7b-af9f-c207c098cfa4"},
        {id:100000273909184,firstName:"Carina",lastName:"Bootell",deviceToken:"4a6202b213ea58fa1649def2c2fec702dbff7bb781a0372ad60c2925ddd5cf03",authToken:"c7358693-3a3e-4e00-8c0b-2b0e023df62f"},
        {id:100000273909188,firstName:"Bill",lastName:"Dechelette",deviceToken:"6cedb5a42a213019a4ef9b49cac86000ffc2a4da310663ab54b1f50e497f8eda",authToken:"50252f92-312a-492b-a639-b0599186af13"},
        {id:100000273909105,firstName:"Moises",lastName:"Ollerhead",deviceToken:"d834a9e2f11ec34bcee2e5901cd8dccdcd5f171b66125016c74d5fad0f4d67c4",authToken:"f44fb05f-1a21-4709-9b12-d9b4bb473835"},
        {id:100000273909076,firstName:"Edmon",lastName:"Lightbowne",deviceToken:"955b197f6678ed3713c0e9b61a53cac86944ce3f3f159e64924e7fdf226422ea",authToken:"0afa33a0-fe42-47e5-b048-392ca5a10443"},
        {id:100000273909137,firstName:"Tamera",lastName:"Denholm",deviceToken:"527386bed6b372337d1d6c8feef5bea4def65d378f7b0a78453bb6980e3d357d",authToken:"9682c4bf-0819-495f-b324-098db56b3a0c"},
        {id:100000273909238,firstName:"Ninon",lastName:"Palek",deviceToken:"d80c14e7fed3ec3c94d1a0fcace682cc236c27e7f928af6b08e73b14e0ce61a5",authToken:"5317e178-10b2-40ee-9a89-be1af7bba2ae"},
        {id:100000273909079,firstName:"Merell",lastName:"Shackesby",deviceToken:"ce456e3e7047fec40142d9da23c86d96426d342f9e0509fcabe2c3a59f2bc5e2",authToken:"c3a59a65-33fc-4871-b15c-ffd0c29429af"},
        {id:100000273909065,firstName:"Kaia",lastName:"Peagrim",deviceToken:"d2e1a18ff1c23ef43288cbc0714adc5f8c524c9c5268714a4dd202706691cdec",authToken:"d6a40af2-3fe0-401b-ae37-0587a14bcea8"},
        {id:100000273909115,firstName:"Takis",lastName:"Truluck",deviceToken:"5d78677c009cc7039a4257b390e61d273754fdaea6861b450c09c3aad30e30e3",authToken:"90ca3fea-db05-4882-b042-23dbc5335319"},
        {id:100000273909167,firstName:"Thornie",lastName:"Geill",deviceToken:"25dae3707c1f3d84216e529e0744d9f68ca9695c1887ed9a540414d1737f931f",authToken:"8f6859a1-db21-4355-9749-8992af2a8115"},
        {id:100000273909205,firstName:"Janel",lastName:"Handforth",deviceToken:"d0e8e48ca57dad6c8a90014ff1e438009739959c3c0f1382f4840a5d5b83adfa",authToken:"f516dc6d-2008-4b26-8d3c-42e5dfb65fa9"},
        {id:100000273909219,firstName:"Trey",lastName:"Keepe",deviceToken:"16af09eb7d39da868eb850cf2d328506487d6ef94b107e66ca7c073ab2f10dd0",authToken:"5028e89a-d0a0-4f9d-aaea-888a7e43718e"},
        {id:100000273909197,firstName:"Boothe",lastName:"Jager",deviceToken:"0774af2f70ecabbd54b871b3a73a8f1b45201bf6546faa479ec4b4cadc5346c8",authToken:"14a93d13-a192-49f1-bbf0-871a1242469e"},
        {id:100000273909170,firstName:"Honey",lastName:"Chomiszewski",deviceToken:"162b687852cd2c9a4d311bad44b92677da7d640fc489f9a89ff1cdd37ec62f8d",authToken:"1d2a0930-f4de-4383-a58d-a56864751809"},
        {id:100000273909173,firstName:"Kylie",lastName:"Fiorentino",deviceToken:"fb3515816c68760db57e5eeac9c7440d668d369541a4420291560e42db34cd0e",authToken:"54b8196c-14b2-490f-912b-be2879595e18"},
        {id:100000273909143,firstName:"Alyda",lastName:"Minker",deviceToken:"f0dadaa3511495e80cae223b3c2c0a7d000ab791371ebfb7790ebe5e9ced7acf",authToken:"be16c321-c7c1-4580-9cf9-eb145a4595d7"}
    ], {});
  
  },

  down: (queryInterface, Sequelize) => {
    /*
      Add reverting commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.bulkDelete('Person', null, {});
    */
  }
};
