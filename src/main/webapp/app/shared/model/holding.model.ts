import { IImage } from 'app/shared/model//image.model';
import { ILocation } from 'app/shared/model//location.model';
import { IUser } from 'app/core/user/user.model';

export interface IHolding {
    id?: number;
    siret?: string;
    name?: string;
    description?: string;
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
        public image?: IImage,
        public location?: ILocation,
        public owner?: IUser
    ) {}
}
