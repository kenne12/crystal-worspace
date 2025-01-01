FROM node:22 as build-stage

ARG BUILD_ENV=production
WORKDIR /app

COPY package*.json ./

RUN npm install --force
RUN npm install -g @angular/cli@19 --force

COPY . .

RUN npm run build --configuration=${BUILD_ENV}

FROM nginx:alpine

COPY --from=build-stage app/nginx.conf /etc/nginx/nginx.conf

COPY --from=build-stage /app/dist/geopoint-ui/browser /usr/share/nginx/html

EXPOSE 80
