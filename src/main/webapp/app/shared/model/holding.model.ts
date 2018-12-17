import { IImage } from 'app/shared/model/image.model';
import { ILocation } from 'app/shared/model/location.model';
import { IUser } from 'app/core';

export interface IHolding {
    id?: number;
    siret?: string;
    name?: string;
    description?: string;
    imageId?: number;
    locationId?: number;
    ownerId?: number;
    image?: IImage;
    location?: ILocation;
    owner?: IUser;
}

export class Holding implements IHolding {
    constructor(
        public id?: number,
        public siret?: string,
        public name?: string,
        public description?: string,
        public imageId?: number,
        public locationId?: number,
        public ownerId?: number,
        public image?: IImage,
        public owner?: IUser,
        public location?: ILocation
    ) {}
}
