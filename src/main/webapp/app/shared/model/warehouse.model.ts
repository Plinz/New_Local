import { IImage } from 'app/shared/model/image.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IWarehouse {
    id?: number;
    name?: string;
    description?: string;
    tel?: string;
    imageId?: number;
    locationId?: number;
    image?: IImage;
    location?: ILocation;
}

export class Warehouse implements IWarehouse {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public tel?: string,
        public imageId?: number,
        public locationId?: number,
        public image?: IImage,
        public location?: ILocation
    ) {}
}
